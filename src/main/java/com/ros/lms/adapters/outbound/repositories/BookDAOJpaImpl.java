package com.ros.lms.adapters.outbound.repositories;

import com.ros.lms.domain.entities.Book;
import com.ros.lms.ports.outbound.repository_contracts.BookDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

}
