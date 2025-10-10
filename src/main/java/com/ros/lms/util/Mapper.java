package com.ros.lms.util;

import com.ros.lms.domain.dtos.AddBookDTO;
import com.ros.lms.domain.dtos.AddMemberDTO;
import com.ros.lms.domain.dtos.AuthorDTO;
import com.ros.lms.domain.dtos.BookDTO;
import com.ros.lms.domain.entities.*;
import com.ros.lms.domain.enums.GenreType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Mapper {

    public final Function<AddBookDTO, Book> addBookDtoToBook = addBookDTO ->
            new Book(addBookDTO.isbn(), addBookDTO.title(), true);

    private final Function<Author, AuthorDTO> authorToAuthorDto =
            entity -> new AuthorDTO(entity.getFirstName(), entity.getLastName());

    public final Function<Book, BookDTO> bookToBookDto = entity -> {

        Set<AuthorDTO> authors = Optional.ofNullable(entity.getAuthors())
                .orElse(List.of())
                .stream()
                .map(authorToAuthorDto)
                .collect(Collectors.toSet());


        Set<GenreType> genres = Optional.ofNullable(entity.getGenres())
                .orElse(List.of())
                .stream()
                .map(Genre::getLabel)
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

    public Member addMemberDtoToMember(AddMemberDTO dto, User user){
        String[] nameParts = dto.firstName().split(" ", 2);
        String firstName = nameParts[0];
        String middleName = nameParts.length > 1 ? nameParts[1] : null;

        Member member = new Member.Builder()
                .governmentID(dto.governmentID())
                .user(user)
                .firstName(firstName)
                .lastName(dto.lastName())
                .phone(dto.phone())
                .dateOfBirth(dto.dateOfBirth())
                .sex(dto.sex())
                .build();

        if(middleName != null)
            member.setMiddleName(middleName);

        return member;
    }

    public User addMemberDtoToUser(AddMemberDTO dto, AuthorityType authorityType, PasswordEncoder passwordEncoder){
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        String hashedPassword = passwordEncoder.encode(dto.password());
        user.setPassword(hashedPassword);
        user.setEnabled(true);
        Set<AuthorityType> authorities = Set.of(authorityType);
        user.setAuthorities(authorities);
        return user;
    }


}
