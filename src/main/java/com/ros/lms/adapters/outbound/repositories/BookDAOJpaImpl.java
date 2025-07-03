package com.ros.lms.adapters.outbound.repositories;

import com.ros.lms.domain.entities.Book;
import com.ros.lms.ports.outbound.repository_contracts.BookDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import jakarta.persistence.Query;

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
    public Optional<Book> findByISBN(long isbn) {

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
    public Page<Book> findAllOrderedByTitle(String title, String genre, String authorFirstName, String authorLastName, Pageable pageable) {
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
        if (genre != null && !genre.isBlank()) {
            countJpql += " AND UPPER(g.description) LIKE UPPER(:genre)";
            selectJpql += " AND UPPER(g.description) LIKE UPPER(:genre)";
            params.put("genre", "%" + genre + "%"); // contains anywhere
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

}
