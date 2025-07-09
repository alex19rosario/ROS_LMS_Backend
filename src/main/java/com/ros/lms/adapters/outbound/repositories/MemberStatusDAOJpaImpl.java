package com.ros.lms.adapters.outbound.repositories;

import com.ros.lms.domain.enums.MemberStatuses;
import com.ros.lms.domain.views.MemberStatusView;
import com.ros.lms.ports.outbound.repository_contracts.MemberStatusDAO;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberStatusDAOJpaImpl implements MemberStatusDAO {

    private final EntityManager entityManager;

    @Autowired
    public MemberStatusDAOJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<MemberStatuses> findStatusByMemberId(long memberId) {
        String query = "SELECT v FROM MemberStatusView v WHERE v.memberId = :memberId";
        try {
            MemberStatusView view = entityManager.createQuery(query, MemberStatusView.class)
                    .setParameter("memberId", memberId)
                    .getSingleResult();

            MemberStatuses status = MemberStatuses.fromValue(view.getMemberStatus());
            return Optional.of(status);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
