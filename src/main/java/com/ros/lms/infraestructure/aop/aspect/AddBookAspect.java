package com.ros.lms.infraestructure.aop.aspect;


import com.ros.lms.domain.dtos.AddBookDTO;
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

    @Pointcut("execution(public void com.ros.inbound.controllers.BookController.addBook(..))")
    private void forAddBookMethod(){}

    @AfterReturning("forAddBookMethod()")
    public void afterReturningAddBookAdvice(JoinPoint joinPoint){
        AddBookDTO bookDTO = (AddBookDTO) joinPoint.getArgs()[0];
        bookAuditService.logAddBookAfterReturning(Long.toString(bookDTO.ISBN()));
    }

    @AfterThrowing("forAddBookMethod()")
    public void afterThrowingAddBookAdvice(){
        bookAuditService.logAddBookAfterThrowing("An error occurred while adding a book");
    }

}
