package com.ros.lms.adapters.outbound.repositories;

import com.ros.lms.domain.entities.Member;
import com.ros.lms.domain.enums.MemberValidationStatus;
import com.ros.lms.ports.outbound.repository_contracts.MemberDAO;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

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

    @Override
    public Set<MemberValidationStatus> validateMemberUniqueness(String governmentID, String username, String email) {
        String query = """
        SELECT m FROM Member m
        WHERE m.governmentID = :governmentID
           OR m.user.username = :username
           OR m.user.email = :email
        """;

        var results = entityManager.createQuery(query, Member.class)
                .setParameter("governmentID", governmentID)
                .setParameter("username", username)
                .setParameter("email", email)
                .getResultList();

        Set<MemberValidationStatus> conflicts = EnumSet.noneOf(MemberValidationStatus.class);

        for (Member m : results) {
            if (m.getGovernmentID().equals(governmentID)) {
                conflicts.add(MemberValidationStatus.GOVERNMENT_ID_EXISTS);
            }
            if (m.getUser().getUsername().equals(username)) {
                conflicts.add(MemberValidationStatus.USERNAME_EXISTS);
            }
            if (m.getUser().getEmail().equals(email)) {
                conflicts.add(MemberValidationStatus.EMAIL_EXISTS);
            }
        }

        return conflicts;
    }

}
