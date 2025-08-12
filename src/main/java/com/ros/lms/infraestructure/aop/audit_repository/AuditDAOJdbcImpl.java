package com.ros.lms.infraestructure.aop.audit_repository;

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
                log.description(),
                log.actionType(),
                log.staffUsername(),
                log.memberUsername(),
                log.bookIsbn(),
                log.loanId()
        );
    }
}
