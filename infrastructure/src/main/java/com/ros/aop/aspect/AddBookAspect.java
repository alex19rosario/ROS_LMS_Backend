package com.ros.aop.aspect;

import com.ros.aop.audit_repository.AuditDAO;
import com.ros.aop.audit_repository.CustomLog;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AddBookAspect {

    private final AuditDAO auditDAO;

    @Autowired
    public AddBookAspect(@Qualifier("auditDAOJdbcImpl") AuditDAO auditDAO){
        this.auditDAO = auditDAO;
    }

    @Pointcut("execution(public void com.ros.inbound.controllers.BookController.addBook(..))")
    private void forAddBookMethod(){}

    @AfterReturning("forAddBookMethod()")
    public void afterReturningAddBookAdvice(){
        CustomLog log = new CustomLog( null, "NEW BOOK WAS ADDED");
        auditDAO.createLog(log);
    }

    @AfterThrowing("forAddBookMethod()")
    public void afterThrowingAddBookAdvice(){
        CustomLog log = new CustomLog(null,"ERROR");
        auditDAO.createLog(log);
    }

}
