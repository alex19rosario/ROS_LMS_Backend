package com.ros.lms.infraestructure.aop.audit_service.contracts;

import com.ros.lms.domain.dtos.AddMemberDTO;

public interface MemberAuditService {
    void logAddMemberAfterReturning(AddMemberDTO addMemberDTO);
    void logAddMemberAfterThrowing(AddMemberDTO addMemberDTO, String description);
}
