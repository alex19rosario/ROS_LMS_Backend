package com.ros.ports_inbound.serviceImpl;

import com.ros.dtos.AddBookDTO;
import com.ros.dtos.AuthorDTO;
import com.ros.entities.Author;
import com.ros.entities.Book;
import com.ros.entities.Genre;
import com.ros.exceptions.BookAlreadyExistException;
import com.ros.ports_inbound.service.BookService;
import com.ros.ports_outbound.dao.AuthorDAO;
import com.ros.ports_outbound.dao.BookDAO;
import com.ros.ports_outbound.dao.GenreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
public class BookServiceImpl implements BookService {

    private final BookDAO bookDAO;
    private final AuthorDAO authorDAO;
    private final GenreDAO genreDAO;

    @Autowired
    public BookServiceImpl(
            @Qualifier("bookDAOJpaImpl") BookDAO bookDAO,
            @Qualifier("authorDAOJpaImpl")AuthorDAO authorDAO,
            @Qualifier("genreDAOJpaImpl")GenreDAO genreDAO
    ){
        this.bookDAO = bookDAO;
        this.authorDAO = authorDAO;
        this.genreDAO = genreDAO;
    }

    @Transactional
    @Override
    public void save(AddBookDTO dto) throws BookAlreadyExistException {
        //Check if the book exist by ISBN
        Optional<Book> existingBook = bookDAO.findByISBN(dto.ISBN());
        if(existingBook.isPresent())
            throw new BookAlreadyExistException("Book already exists in the database.");

        // Map the DTO to a Book entity
        Book book = bookMapper.apply(dto);

        // Iterate over authors to check if they exist
        for (AuthorDTO authorDTO : dto.authors()) {
            Optional<Author> existingAuthor = authorDAO.findByFullName(
                    authorDTO.firstName(),
                    authorDTO.middleName(),
                    authorDTO.lastName()
            );

            if (existingAuthor.isPresent()) {
                // Associate existing author with the book
                book.addAuthor(existingAuthor.get());
            } else {
                // Create a new author and associate it with the book
                Author newAuthor = new Author(
                        authorDTO.firstName(),
                        authorDTO.middleName(),
                        authorDTO.lastName()
                );
                book.addAuthor(newAuthor);
            }
        }

        //Iterate over the genres
        for (String desc: dto.genres()){
            Optional<Genre> genre = genreDAO.findByDescription(desc);
            genre.ifPresent(book::addGenre);
        }


        // Save the book along with any new authors
        bookDAO.create(book);
    }

    private final Function<AddBookDTO, Book> bookMapper = addBookDTO -> {
        return new Book(addBookDTO.ISBN(), addBookDTO.title(), 'Y');
    };

}
