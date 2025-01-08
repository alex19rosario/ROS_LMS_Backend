package com.ros.aop.aspect;

import com.ros.aop.audit_entity.CustomLog;
import com.ros.aop.audit_service.service.BookAuditService;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AddBookAspect {

    private final BookAuditService bookAuditService;

    @Autowired
    public AddBookAspect(@Qualifier("bookAuditServiceImpl") BookAuditService  bookAuditService){
        this.bookAuditService = bookAuditService;
    }

    @Pointcut("execution(public void com.ros.inbound.controllers.BookController.addBook(..))")
    private void forAddBookMethod(){}

    @AfterReturning("forAddBookMethod()")
    public void afterReturningAddBookAdvice(){
        CustomLog log = new CustomLog.Builder( "NEW BOOK WAS ADDED")
                .build();
        bookAuditService.logAddBookAfter(log);
    }

    @AfterThrowing("forAddBookMethod()")
    public void afterThrowingAddBookAdvice(){
        CustomLog log = new CustomLog.Builder("ERROR")
                .build();
        bookAuditService.logAddBookAfter(log);
    }

}
