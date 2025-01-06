package com.ros.outbound.repositories;

import com.ros.entities.Author;
import com.ros.ports_outbound.dao.AuthorDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthorDAOJpaImpl implements AuthorDAO {

    private final EntityManager entityManager;

    @Autowired
    public AuthorDAOJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Author> findByFullName(String firstName, String middleName, String lastName) {

        if (middleName == null || middleName.trim().isEmpty()){
            return findByFirstAndLastName(firstName, lastName);
        } else {
            return findByFirstAndMiddleAndLastName(firstName, middleName, lastName);
        }
    }

    @Override
    public Optional<Author> findByFirstAndLastName(String firstName, String lastName) {

        String query = "SELECT a FROM Author a WHERE a.firstName = :firstName AND a.lastName = :lastName";

        try {
            Author author = entityManager.createQuery(query, Author.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName)
                    .getSingleResult();

            return Optional.ofNullable(author);
        } catch (NoResultException e){
            return Optional.empty();
        }

    }

    @Override
    public Optional<Author> findByFirstAndMiddleAndLastName(String firstName, String middleName, String lastName) {
        String query = "SELECT a FROM Author a WHERE a.firstName = :firstName AND a.middleName = :middleName AND a.lastName = :lastName";

        try {
            Author author = entityManager.createQuery(query, Author.class)
                    .setParameter("firstName", firstName)
                    .setParameter("middleName", middleName)
                    .setParameter("lastName", lastName)
                    .getSingleResult();

            return Optional.ofNullable(author);
        } catch (NoResultException e){
            return Optional.empty();
        }
    }
}
