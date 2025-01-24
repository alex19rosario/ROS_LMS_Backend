package com.ros;

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
