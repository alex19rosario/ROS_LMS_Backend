package com.ros.aop.audit_repository.daoImpl;

import com.ros.aop.audit_entity.CustomLog;
import com.ros.aop.audit_repository.dao.AuditDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuditDAOJdbcImpl implements AuditDAO {

    private final JdbcTemplate jdbcTemplate;

    public AuditDAOJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void createLog(CustomLog log) {
        String procedureCall = "{call GENERATE_LOG(?, ?, ?, ?, ?, ?)}";

        jdbcTemplate.update(procedureCall,
                log.getDescription(), // P_DESCRIPTION
                log.getStaffId(),                      // P_STAFF_ID
                log.getActionType(),                    // P_ACTION_TYPE
                log.getMemberId(),                     // P_MEMBER_ID
                log.getBookId(),                      // P_BOOK_ID
                log.getLoanId()                      // P_LOAN_ID
        );
        System.out.println(log);
    }
}
