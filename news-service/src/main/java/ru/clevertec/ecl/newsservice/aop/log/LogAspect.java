package ru.clevertec.ecl.newsservice.aop.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class LogAspect {

    @Pointcut("execution(@ru.clevertec.ecl.newsservice.aop.annotation.Log * *(..))")
    private void annotatedMethods() {
    }

    @Pointcut("within(@ru.clevertec.ecl.newsservice.aop.annotation.Log *)")
    private void annotatedClass() {
    }

    @Around("annotatedMethods() || annotatedClass()")
    public Object writeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object methodResult = joinPoint.proceed(joinPoint.getArgs());

        String logMessage = "Called method: " + joinPoint.getTarget().getClass().getSimpleName() +
                " " + joinPoint.getSignature().getName() + "\n" +
                "request: " + Arrays.toString(joinPoint.getArgs()) + '\n' +
                "response: " + methodResult;
        log.info(logMessage);

        return methodResult;
    }
}
