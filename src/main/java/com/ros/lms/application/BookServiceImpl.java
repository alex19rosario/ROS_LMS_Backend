package com.ros.lms.application;

import com.ros.lms.domain.dtos.AddBookDTO;
import com.ros.lms.domain.dtos.AuthorDTO;
import com.ros.lms.domain.dtos.BookDTO;
import com.ros.lms.domain.dtos.RenamedMultipartFile;
import com.ros.lms.domain.entities.Author;
import com.ros.lms.domain.entities.Book;
import com.ros.lms.domain.entities.Genre;
import com.ros.lms.domain.enums.GenreType;
import com.ros.lms.domain.exceptions.BookAlreadyExistsException;
import com.ros.lms.domain.exceptions.PageOutOfRangeException;
import com.ros.lms.domain.exceptions.StorageException;
import com.ros.lms.ports.inbound.service_contracts.BookService;
import com.ros.lms.ports.inbound.service_contracts.StorageService;
import com.ros.lms.ports.outbound.repository_contracts.AuthorDAO;
import com.ros.lms.ports.outbound.repository_contracts.BookDAO;
import com.ros.lms.ports.outbound.repository_contracts.GenreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookDAO bookDAO;
    private final AuthorDAO authorDAO;
    private final GenreDAO genreDAO;
    private final StorageService storageService;
    private static final int MAX_PAGE_SIZE = 100;

    @Autowired
    public BookServiceImpl(
            @Qualifier("bookDAOJpaImpl") BookDAO bookDAO,
            @Qualifier("authorDAOJpaImpl")AuthorDAO authorDAO,
            @Qualifier("genreDAOJpaImpl")GenreDAO genreDAO,
            StorageService storageService
    ){
        this.bookDAO = bookDAO;
        this.authorDAO = authorDAO;
        this.genreDAO = genreDAO;
        this.storageService = storageService;
    }

    @Transactional
    @CacheEvict(value = "booksCache", allEntries = true)
    @Override
    public void add(AddBookDTO addBookDTO) throws BookAlreadyExistsException, StorageException {
        // Check if the book exist by ISBN
        Optional<Book> existingBook = bookDAO.findByISBN(addBookDTO.ISBN());
        if(existingBook.isPresent())
            throw new BookAlreadyExistsException("Book already exists in the database.");

        // Map the DTO to a Book entity
        Book book = addBookMapper.apply(addBookDTO);
        Set<AuthorDTO> authors = parseAuthors(addBookDTO.authors());
        // Iterate over authors to check if they exist
        for (AuthorDTO authorDTO : authors) {
            // Split the firstName into first and middle names, if available
            String[] nameParts = authorDTO.firstName().split(" ", 2);
            String firstName = nameParts[0];
            String middleName = nameParts.length > 1 ? nameParts[1] : null;

            Optional<Author> existingAuthor = authorDAO.findByFullName(
                    firstName,
                    middleName,
                    authorDTO.lastName()
            );

            if (existingAuthor.isPresent()) {
                // Associate existing author with the book
                book.addAuthor(existingAuthor.get());
            } else {
                // Create a new author and associate it with the book
                Author newAuthor = new Author(
                        firstName,
                        middleName,
                        authorDTO.lastName()
                );
                book.addAuthor(newAuthor);
            }
        }

        Set<String> genres = parseGenres(addBookDTO.genres());
        for (String desc : genres) {
            try {
                GenreType genreType = GenreType.valueOf(desc.toUpperCase());
                Optional<Genre> genre = genreDAO.findByDescription(genreType);
                genre.ifPresent(book::addGenre);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid genre provided: " + desc);
            }
        }

        // Handle file upload
        String filename = null;
        if (addBookDTO.coverImage() != null && !addBookDTO.coverImage().isEmpty()) {
            try {
                // Generate unique filename while preserving extension
                String originalFilename = Objects.requireNonNull(addBookDTO.coverImage().getOriginalFilename());
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                filename = UUID.randomUUID() + extension;

                // Create a renamed version of the file
                MultipartFile renamedFile = new RenamedMultipartFile(addBookDTO.coverImage(), filename);

                // Store the file
                storageService.store(renamedFile);
            } catch (Exception e) {
                throw new StorageException("Failed to store cover image: " + e.getMessage());
            }
        }

        book.setCoverImagePath(filename);
        // Save the book along with any new authors
        bookDAO.create(book);
    }

    @Cacheable(
            value = "booksCache",
            key = "'page=' + #page + '&size=' + #size + '&title=' + #title + '&genre=' + #genre + '&first=' + #authorFirstName + '&last=' + #authorLastName"
    )
    @Override
    public Page<BookDTO> getAll(int page, int size, String title, String genre, String authorFirstName, String authorLastName) throws PageOutOfRangeException {

        if(page < 0) throw new PageOutOfRangeException("Page number cannot be negative");

        else if(size <= 0) throw new PageOutOfRangeException("Page size must be greater than 0");

        else if(size > MAX_PAGE_SIZE) throw new PageOutOfRangeException("Page size cannot exceed " + MAX_PAGE_SIZE);

        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookDAO.findAllOrderedByTitle(title, genre, authorFirstName, authorLastName, pageable);
        return bookPage.map(bookDTOMapper);
    }

    private final Function<AddBookDTO, Book> addBookMapper = addBookDTO ->
            new Book(addBookDTO.ISBN(), addBookDTO.title(), true);

    private Set<AuthorDTO> parseAuthors(String authorsString) {
        return Arrays.stream(authorsString.split(","))
                .map(authorString -> {
                    String[] parts = authorString.split("-");
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Invalid author format: " + authorString);
                    }
                    return new AuthorDTO(parts[0].trim(), parts[1].trim());
                })
                .collect(Collectors.toSet());
    }

    private Set<String> parseGenres(String genresString) {
        return Arrays.stream(genresString.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }

    private final Function<Author, AuthorDTO> authorDTOMapper =
            entity -> new AuthorDTO(entity.getFirstName(), entity.getLastName());

    private final Function<Book, BookDTO> bookDTOMapper = entity -> {

        Set<AuthorDTO> authors = Optional.ofNullable(entity.getAuthors())
                .orElse(List.of())
                .stream()
                .map(authorDTOMapper)
                .collect(Collectors.toSet());


        Set<String> genres = Optional.ofNullable(entity.getGenres())
                .orElse(List.of())
                .stream()
                .map(Genre::getDescription)
                .map(GenreType::getLabel)
                .collect(Collectors.toSet());

        return new BookDTO(
                entity.getId(),
                entity.getIsbn(),
                entity.getTitle(),
                authors,
                genres,
                entity.isAvailable(),
                entity.getCoverImagePath()
                );
    };


}
