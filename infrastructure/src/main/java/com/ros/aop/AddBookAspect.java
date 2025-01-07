package com.ros.aop;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AddBookAspect {

    @Pointcut("execution(public void com.ros.inbound.controllers.BookController.addBook(..))")
    private void forAddBookMethod(){}

    @AfterReturning("forAddBookMethod()")
    public void afterReturningAddBookAdvice(){
        System.out.println("\n\n\n\n=====> SUCCESS...");
    }

    @AfterThrowing("forAddBookMethod()")
    public void afterThrowingAddBookAdvice(){
        System.out.println("\n\n\n\n=====> FAILURE...");
    }

}
