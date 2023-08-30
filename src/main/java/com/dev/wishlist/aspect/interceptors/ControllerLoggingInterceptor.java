package com.dev.wishlist.aspect.interceptors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ControllerLoggingInterceptor {

    private final Logger logger = LoggerFactory.getLogger(ControllerLoggingInterceptor.class);

    @Around("@annotation(com.dev.wishlist.aspect.annotations.ControllerLogging)")
    public Object logControllerExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        var methodSignature = (MethodSignature) joinPoint.getSignature();
        var method = methodSignature.getMethod();
        var parameters = method.getParameters();
        var arguments = joinPoint.getArgs();
        var input = mapAgrsAndParamsIntoInput(parameters, arguments);
        Class<?> interceptedClass = joinPoint.getTarget().getClass();
        String path = interceptedClass.getAnnotation(RequestMapping.class) != null ?
                interceptedClass.getAnnotation(RequestMapping.class).value()[0] : "";
        String endpoint = method.getAnnotation(PostMapping.class) != null ?
                method.getAnnotation(PostMapping.class).value()[0] : "";

        logger.info("endpoint={}, request_input={}", String.format("%s%s", path, endpoint), input.entrySet());

        Object proceed = joinPoint.proceed();

        logger.info("response_test={}", proceed);

        return proceed;
    }

    private Map<String, Object> mapAgrsAndParamsIntoInput(Parameter[] parameters, Object[] arguments) {
        var input = new HashMap<String, Object>();

        for (int i = 0; i < parameters.length; i++) {
            input.put(parameters[i].getName(), arguments[i]);
        }

        return input;
    }
}
