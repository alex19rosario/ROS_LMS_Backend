package com.ros.lms.adapters.outbound.repositories;

import com.ros.lms.domain.entities.Book;
import com.ros.lms.domain.enums.GenreType;
import com.ros.lms.ports.outbound.repository_contracts.BookDAO;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookDAOJpaImpl implements BookDAO {

    private final EntityManager entityManager;

    @Autowired
    public BookDAOJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void create(Book book) {
        entityManager.persist(book);
    }

    @Override
    public Optional<Book> findByISBN(String isbn) {

        String query = "SELECT b FROM Book b WHERE b.isbn = :isbn";
        try{
            Book book = entityManager.createQuery(query, Book.class)
                    .setParameter("isbn", isbn)
                    .getSingleResult();

            return Optional.ofNullable(book);
        } catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public Page<Book> findAllOrderedByTitle(String title, GenreType genre, String authorFirstName, String authorLastName, Boolean isAvailable, Pageable pageable) {
        // Base JPQL strings
        String countJpql = "SELECT COUNT(DISTINCT b) FROM Book b " +
                "LEFT JOIN b.genres g " +
                "LEFT JOIN b.authors a " +
                "WHERE 1=1";
        String selectJpql = "SELECT DISTINCT b FROM Book b " +
                "LEFT JOIN b.genres g " +
                "LEFT JOIN b.authors a " +
                "WHERE 1=1";

        Map<String, Object> params = new HashMap<>();

        // Title filter
        if (title != null && !title.isBlank()) {
            countJpql += " AND UPPER(b.title) LIKE UPPER(:title)";
            selectJpql += " AND UPPER(b.title) LIKE UPPER(:title)";
            params.put("title", "%" + title + "%"); // contains anywhere
        }

        // Genre filter
        if (genre != null) {
            countJpql += " AND g.label = :genre";
            selectJpql += " AND g.label = :genre";
            params.put("genre", genre); // <-- Use the GenreType enum directly
        }

        // Author first/middle name filter
        if (authorFirstName != null && !authorFirstName.isBlank()) {
            countJpql += " AND (UPPER(a.firstName) LIKE UPPER(:authorFirstName) OR UPPER(a.middleName) LIKE UPPER(:authorFirstName))";
            selectJpql += " AND (UPPER(a.firstName) LIKE UPPER(:authorFirstName) OR UPPER(a.middleName) LIKE UPPER(:authorFirstName))";
            params.put("authorFirstName", "%" + authorFirstName + "%");
        }

        // Author last name filter
        if (authorLastName != null && !authorLastName.isBlank()) {
            countJpql += " AND UPPER(a.lastName) LIKE UPPER(:authorLastName)";
            selectJpql += " AND UPPER(a.lastName) LIKE UPPER(:authorLastName)";
            params.put("authorLastName", "%" + authorLastName + "%");
        }

        // Availability filter
        if (isAvailable != null) {
            countJpql += " AND b.isAvailable = :isAvailable";
            selectJpql += " AND b.isAvailable = :isAvailable";
            params.put("isAvailable", isAvailable);
        }

        // Order by title
        selectJpql += " ORDER BY b.title ASC";

        // Count query
        Query countQuery = entityManager.createQuery(countJpql);
        params.forEach(countQuery::setParameter);
        long total = (Long) countQuery.getSingleResult();

        // Data query
        TypedQuery<Book> query = entityManager.createQuery(selectJpql, Book.class);
        params.forEach(query::setParameter);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(query.getResultList(), pageable, total);
    }

    @Override
    public Optional<Book> findById(long id) {
        try {
            Book book = entityManager.find(Book.class, id);
            return Optional.ofNullable(book);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Book book) {
        entityManager.merge(book);
    }


}
