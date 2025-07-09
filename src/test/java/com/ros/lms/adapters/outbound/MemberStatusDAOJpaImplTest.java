package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.MemberStatusDAOJpaImpl;
import com.ros.lms.domain.enums.MemberStatuses;
import com.ros.lms.domain.views.MemberStatusView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class MemberStatusDAOJpaImplTest {

    private EntityManager entityManager;
    private MemberStatusDAOJpaImpl memberStatusDAO;

    @BeforeEach
    void setUp() {
        entityManager = mock(EntityManager.class);
        memberStatusDAO = new MemberStatusDAOJpaImpl(entityManager);
    }

    @Test
    void testFindStatusByMemberId_shouldReturnStatus() {
        long memberId = 1L;
        String expectedStatus = "HAS-LOAN";

        // Mock the query and its result
        TypedQuery<MemberStatusView> mockQuery = mock(TypedQuery.class);
        MemberStatusView mockView = mock(MemberStatusView.class);
        when(mockView.getMemberStatus()).thenReturn(expectedStatus);

        when(entityManager.createQuery(anyString(), eq(MemberStatusView.class)))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter(eq("memberId"), eq(memberId))).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(mockView);

        // Act
        Optional<MemberStatuses> result = memberStatusDAO.findStatusByMemberId(memberId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(MemberStatuses.HAS_LOAN);
    }

    @Test
    void testFindStatusByMemberId_shouldReturnEmptyWhenNotFound() {
        long memberId = 999L;

        TypedQuery<MemberStatusView> mockQuery = mock(TypedQuery.class);

        when(entityManager.createQuery(anyString(), eq(MemberStatusView.class)))
                .thenReturn(mockQuery);
        when(mockQuery.setParameter(eq("memberId"), eq(memberId))).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenThrow(new RuntimeException("Not found"));

        Optional<MemberStatuses> result = memberStatusDAO.findStatusByMemberId(memberId);

        assertThat(result).isEmpty();
    }
}
