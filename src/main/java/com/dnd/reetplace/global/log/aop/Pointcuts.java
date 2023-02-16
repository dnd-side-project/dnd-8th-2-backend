package com.dnd.reetplace.global.log.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.dnd.reetplace.app.controller..*(..))")
    public void controllerPoint(){}

    @Pointcut("execution(* com.dnd.reetplace.app.service..*(..))")
    public void servicePoint(){}

    @Pointcut("execution(* com.dnd.reetplace.app.repository..*(..))")
    public void repositoryPoint(){}
}
