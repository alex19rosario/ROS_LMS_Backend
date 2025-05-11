package com.ros.lms.infraestructure.aop.aspect;

import com.ros.lms.infraestructure.aop.audit_service.MemberAuditService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AddMemberAspect {

    private final MemberAuditService memberAuditService;

    @Autowired
    public AddMemberAspect(@Qualifier("memberAuditServiceImpl") MemberAuditService memberAuditService){
        this.memberAuditService = memberAuditService;
    }

    @Pointcut("execution(public void com.ros.inbound.controllers.MemberController.addMember(..))")
    private void forAddMemberMethod(){}

    @AfterReturning("forAddMemberMethod()")
    public void afterReturningAddMemberAdvice(){
        memberAuditService.logAddMemberAfterReturning(null);
    }

    @AfterThrowing("forAddMemberMethod()")
    public void afterThrowingAddMemberAdvice(){
        memberAuditService.logAddMemberAfterThrowing("An error occurred while adding a member");
    }

}
