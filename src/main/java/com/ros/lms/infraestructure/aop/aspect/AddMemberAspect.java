package com.ros.lms.infraestructure.aop.aspect;

import com.ros.lms.domain.dtos.AddMemberDTO;
import com.ros.lms.infraestructure.aop.audit_service.contracts.MemberAuditService;
import org.aspectj.lang.JoinPoint;
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

    @Pointcut("execution(public void com.ros.lms.ports.inbound.service_contracts.MemberService.add(..))")
    private void forAddMemberMethod(){}

    @AfterReturning("forAddMemberMethod()")
    public void afterReturningAddMemberAdvice(JoinPoint joinPoint){
        AddMemberDTO memberDTO = (AddMemberDTO) joinPoint.getArgs()[0];
        memberAuditService.logAddMemberAfterReturning(memberDTO);
    }

    @AfterThrowing(pointcut = "forAddMemberMethod()", throwing = "ex")
    public void afterThrowingAddMemberAdvice(JoinPoint joinPoint, Throwable ex){
        AddMemberDTO memberDTO = (AddMemberDTO) joinPoint.getArgs()[0];
        String description = "An error occurred while adding member: " + ex.getMessage();
        memberAuditService.logAddMemberAfterThrowing(memberDTO, description);
    }

}
