package com.ros.repositories;

import com.ros.entities.Member;
import com.ros.outbound.repositories.MemberDAOJpaImpl;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")  // ensures it picks application-test.yml
class MemberDAOJpaImplTest {

    @Autowired
    private EntityManager entityManager;

    private MemberDAOJpaImpl memberDAO;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        memberDAO = new MemberDAOJpaImpl(entityManager);
    }

    @Test
    @Transactional
    void testCreateAndFindByGovernmentID() {
        Member member = new Member("GOV123", "John", "", "Doe", "123456789", (byte) 30, 'M', "john@example.com", "johndoe");
        memberDAO.create(member);

        Optional<Member> found = memberDAO.findByGovernmentID("GOV123");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @Transactional
    void testFindByUsername() {
        Member member = new Member("GOV456", "Jane", "", "Smith", "987654321", (byte) 25, 'F', "jane@example.com", "janesmith");
        memberDAO.create(member);

        Optional<Member> found = memberDAO.findByUsername("janesmith");

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Jane");
    }

    @Test
    @Transactional
    void testFindByEmailNotFound() {
        Optional<Member> found = memberDAO.findByEmail("not@found.com");
        assertThat(found).isNotPresent();
    }
}



/*
import com.ros.entities.Member;
import com.ros.outbound.repositories.MemberDAOJpaImpl;
import com.ros.ports_outbound.dao.MemberDAO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class MemberDAOJpaImplTest {
    @Autowired
    private EntityManager entityManager;

    private MemberDAO memberDAO;

    @BeforeEach
    void setUp() {
        memberDAO = new MemberDAOJpaImpl(entityManager);
    }

    @Transactional
    @Test
    void create_shouldPersistMember(){
        // Arrange
        Member member = new Member(
                "123123132",
                "carlos",
                "alexander",
                "rosario sanchez",
                "6474256438",
                (byte) 27,
                'M',
                "test19@gmail.com",
                "carlos19");

        //Act
        memberDAO.create(member);
        entityManager.flush(); // Ensure the entity is persisted

        // Assert
        Member persistedMember = entityManager.find(Member.class, member.getId());

        assertThat(persistedMember).isNotNull();
        assertThat(persistedMember.getGovernmentID()).isEqualTo("123123132");
        assertThat(persistedMember.getFirstName()).isEqualTo("carlos");
        assertThat(persistedMember.getMiddleName()).isEqualTo("alexander");
        assertThat(persistedMember.getLastName()).isEqualTo("rosario sanchez");
        assertThat(persistedMember.getPhone()).isEqualTo("6474256438");
    }

    @Transactional
    @Test
    void findByGovernmentID_shouldReturnMember_whenMemberExists(){
        // Arrange
        Member member = new Member(
                "123123132",
                "carlos",
                "alexander",
                "rosario sanchez",
                "6474256438",
                (byte) 27,
                'M',
                "test19@gmail.com",
                "carlos19");
        entityManager.persist(member);

        // Act
        Optional<Member> foundMember = memberDAO.findByGovernmentID("123123132");

        // Assert
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getFirstName()).isEqualTo("carlos");
    }

    @Transactional
    @Test
    void findByUsername_shouldReturnMember_whenMemberExists(){
        // Arrange
        Member member = new Member(
                "123123132",
                "carlos",
                "alexander",
                "rosario sanchez",
                "6474256438",
                (byte) 27,
                'M',
                "test19@gmail.com",
                "carlos19");
        entityManager.persist(member);

        // Act
        Optional<Member> foundMember = memberDAO.findByUsername("carlos19");

        // Assert
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getFirstName()).isEqualTo("carlos");
    }

    @Transactional
    @Test
    void findByEmail_shouldReturnMember_whenMemberExists(){
        // Arrange
        Member member = new Member(
                "123123132",
                "carlos",
                "alexander",
                "rosario sanchez",
                "6474256438",
                (byte) 27,
                'M',
                "test19@gmail.com",
                "carlos19");
        entityManager.persist(member);

        // Act
        Optional<Member> foundMember = memberDAO.findByEmail("test19@gmail.com");

        // Assert
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getFirstName()).isEqualTo("carlos");
    }


}
*/