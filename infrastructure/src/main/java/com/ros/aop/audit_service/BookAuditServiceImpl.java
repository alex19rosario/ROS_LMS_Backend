package com.ros.aop.audit_service;

import org.springframework.stereotype.Service;

@Service
public class BookAuditServiceImpl implements BookAuditService{

    @Override
    public void logAddBookAfterReturning() {
        System.out.println("\n\n\n===> Logging add book after returning");
    }

    @Override
    public void logAddBookAfterThrowing() {
        System.out.println("\n\n\n===> Logging add book after throwing");
    }
}
