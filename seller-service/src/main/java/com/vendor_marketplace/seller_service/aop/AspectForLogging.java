package com.vendor_marketplace.seller_service.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AspectForLogging {

    @Around("execution(* com.vendor_marketplace.seller_service.controller..*(..))")
    public Object loggerForController(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecution(joinPoint);
    }

    @Around("execution(* com.vendor_marketplace.seller_service.services..*(..))")
    public Object loggerForService(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecution(joinPoint);
    }

    @Around("execution(* com.vendor_marketplace.seller_service.dao..*(..))")
    public Object loggingForDaoLayer(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecution(joinPoint);
    }


    private Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();

        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String method = signature.getName();

        log.info("Calling :: {}.{}()", className, method);

        try {
            Object result = joinPoint.proceed();

            long durationMs = (System.nanoTime() - start) / 1_000_000;
            log.info("End :: {}.{}() ==> duration={}ms", className, method, durationMs);
            return result;
        } catch (Throwable ex) {
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            log.error("Exception in {}.{}() after {}ms: {}", className, method, durationMs, ex.getMessage(), ex);
            throw ex;
        }
    }


}
