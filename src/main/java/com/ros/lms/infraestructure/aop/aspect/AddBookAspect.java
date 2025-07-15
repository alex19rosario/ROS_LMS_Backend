package com.ros.lms.infraestructure.aop.aspect;


import com.ros.lms.infraestructure.aop.audit_service.BookAuditService;
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
public class AddBookAspect {

    private final BookAuditService bookAuditService;

    @Autowired
    public AddBookAspect(@Qualifier("bookAuditServiceImpl") BookAuditService bookAuditService){
        this.bookAuditService = bookAuditService;
    }

    @Pointcut("execution(public void com.ros.lms.adapters.inbound.controllers.BookController.addBook(..))")
    private void forAddBookMethod(){}

    @AfterReturning("forAddBookMethod()")
    public void afterReturningAddBookAdvice(JoinPoint joinPoint){
        String isbn = (String) joinPoint.getArgs()[0];
        String staffUsername = (String) joinPoint.getArgs()[4];
        bookAuditService.logAddBookAfterReturning(staffUsername, isbn);
    }

    @AfterThrowing(pointcut = "forAddBookMethod()", throwing = "ex")
    public void afterThrowingAddBookAdvice(JoinPoint joinPoint, Throwable ex){
        String isbn = (String) joinPoint.getArgs()[0];
        String staffUsername = (String) joinPoint.getArgs()[4];
        String description = "An error occurred while adding book: " + ex.getMessage();
        bookAuditService.logAddBookAfterThrowing(description, isbn, staffUsername);
    }

}
