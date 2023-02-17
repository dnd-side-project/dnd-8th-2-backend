package com.dnd.reetplace.global.log.aop;

import com.dnd.reetplace.global.log.aop.LogTrace;
import com.dnd.reetplace.global.log.aop.TraceStatus;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    @Around("com.dnd.reetplace.global.log.aop.Pointcuts.controllerPoint() || " +
            "com.dnd.reetplace.global.log.aop.Pointcuts.servicePoint() || " +
            "com.dnd.reetplace.global.log.aop.Pointcuts.repositoryPoint()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            // Logic call
            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
