package com.ros.lms.adapters.outbound.repositories;

import com.ros.lms.domain.entities.AuthorityType;
import com.ros.lms.domain.enums.RoleType;
import com.ros.lms.ports.outbound.repository_contracts.AuthorityTypeDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthorityTypeDAOJpaImpl implements AuthorityTypeDAO {

    private final EntityManager entityManager;

    @Autowired
    public AuthorityTypeDAOJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<AuthorityType> findByLabel(RoleType label) {

        String query = "SELECT a FROM AuthorityType a WHERE a.label = :label";

        try {
            AuthorityType authorityType = entityManager.createQuery(query, AuthorityType.class)
                    .setParameter("label", label)
                    .getSingleResult();

            return Optional.ofNullable(authorityType);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
