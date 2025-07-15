package com.ros.lms.infraestructure.aop.audit_service;

import com.ros.lms.domain.dtos.AddMemberDTO;
import com.ros.lms.domain.enums.ActionType;
import com.ros.lms.infraestructure.aop.audit_repository.AuditDAO;
import com.ros.lms.infraestructure.aop.audit_repository.CustomLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MemberAuditServiceImpl implements MemberAuditService {

    private final AuditDAO auditDAO;

    @Autowired
    public MemberAuditServiceImpl(@Qualifier("auditDAOJdbcImpl") AuditDAO auditDAO){
        this.auditDAO = auditDAO;
    }

    @Override
    public void logAddMemberAfterReturning(AddMemberDTO addMemberDTO) {

        CustomLog log = new CustomLog.Builder()
                .staffUsername(addMemberDTO.staffUsername())
                .actionType(ActionType.NEW_MEMBER_WAS_ADDED.getValue())
                .memberUsername(addMemberDTO.username())
                .build();

        auditDAO.createLog(log);
    }

    @Override
    public void logAddMemberAfterThrowing(AddMemberDTO addMemberDTO, String description) {
        CustomLog log = new CustomLog.Builder()
                .description(description)
                .staffUsername(addMemberDTO.staffUsername())
                .actionType(ActionType.ERROR.getValue())
                .memberUsername(addMemberDTO.username())
                .build();

        auditDAO.createLog(log);
    }
}
