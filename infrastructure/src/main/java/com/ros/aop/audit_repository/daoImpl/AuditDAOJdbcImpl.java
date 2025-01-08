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
        String procedureCall = "{call GENERATE_LOG(?, ?)}";

        jdbcTemplate.update(procedureCall,
                log.description(),
                log.actionType()
        );
        System.out.println(log);
    }
}
