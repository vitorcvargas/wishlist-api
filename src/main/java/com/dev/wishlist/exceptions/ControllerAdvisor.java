package com.dev.wishlist.exceptions;

import com.dev.wishlist.mappers.ExceptionResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ControllerAdvisor {

    private final Logger logger = LoggerFactory.getLogger(ControllerAdvisor.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(final BadRequestException ex) {
        return getResponseEntity(ex, BAD_REQUEST);
    }

    private ResponseEntity<ExceptionResponse> getResponseEntity(final GlobalException ex, final HttpStatus httpStatus) {
        final ExceptionResponse response = ExceptionResponseMapper.INSTANCE.globalExceptionToExceptionResponse(ex);
        logger.error("{}", response);
        return new ResponseEntity<>(response, httpStatus);
    }
}
