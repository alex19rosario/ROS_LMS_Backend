package com.ros.lms.adapters.outbound.repositories;

import com.ros.lms.domain.entities.Member;
import com.ros.lms.ports.outbound.repository_contracts.MemberDAO;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberDAOJpaImpl implements MemberDAO {

    private final EntityManager entityManager;

    @Autowired
    public MemberDAOJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void create(Member member) {
        entityManager.persist(member);
    }

    @Override
    public Optional<Member> findByGovernmentID(String governmentID) {

        String query = "SELECT m FROM Member m WHERE m.governmentID = :governmentID";

        try{
            Member member = entityManager.createQuery(query, Member.class)
                    .setParameter("governmentID", governmentID)
                    .getSingleResult();

            return Optional.ofNullable(member);
        } catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByUsername(String username) {

        String query = "SELECT m FROM Member m WHERE m.user.username = :username";

        try{
            Member member = entityManager.createQuery(query, Member.class)
                    .setParameter("username", username)
                    .getSingleResult();

            return Optional.ofNullable(member);
        } catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmail(String email) {

        String query = "SELECT m FROM Member m WHERE m.user.email = :email";

        try{
            Member member = entityManager.createQuery(query, Member.class)
                    .setParameter("email", email)
                    .getSingleResult();

            return Optional.ofNullable(member);
        } catch (Exception e){
            return Optional.empty();
        }
    }
}
