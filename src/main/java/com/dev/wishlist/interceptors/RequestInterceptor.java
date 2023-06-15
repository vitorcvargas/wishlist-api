package com.dev.wishlist.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        log.info("Retrieving requestTraceId...");

        var requestTraceId = request.getHeader("x-request-trace-id");

        addRequestTraceIdToLog(requestTraceId);

        log.info("RequestTraceId retrieved: {}", requestTraceId);

        return true;
    }

    private void addRequestTraceIdToLog(String requestTraceId) {
        ThreadContext.put("requestTraceId", requestTraceId);
    }
}
