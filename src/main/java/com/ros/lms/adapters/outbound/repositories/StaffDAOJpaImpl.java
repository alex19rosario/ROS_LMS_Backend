package com.ros.lms.adapters.outbound.repositories;

import com.ros.lms.domain.entities.Staff;
import com.ros.lms.ports.outbound.repository_contracts.StaffDAO;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StaffDAOJpaImpl implements StaffDAO {

    private final EntityManager entityManager;

    @Autowired
    public StaffDAOJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Staff> findByUsername(String username) {

        String query = "SELECT s FROM Staff s WHERE s.user.username = :username";

        try{
            Staff staff = entityManager.createQuery(query, Staff.class)
                    .setParameter("username", username)
                    .getSingleResult();

            return Optional.ofNullable(staff);
        } catch (Exception e){
            return Optional.empty();
        }
    }
}
