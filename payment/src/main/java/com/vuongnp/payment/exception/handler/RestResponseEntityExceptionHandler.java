package com.vuongnp.payment.exception.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vuongnp.payment.exception.DuplicateIdempotentKeyException;
import com.vuongnp.payment.exception.NotEnoughBalanceException;
import com.vuongnp.payment.exception.TooManyRequestException;
import com.vuongnp.payment.exception.UserNotFoundException;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
    private static final String ERROR_CODE_INTERNAL = "INTERNAL_ERROR";
    private static final Map<Class<? extends RuntimeException>, HttpStatus> EXCEPTION_TO_HTTP_STATUS_CODE = Map.of(
        UserNotFoundException.class, HttpStatus.NOT_FOUND,
        DuplicateIdempotentKeyException.class, HttpStatus.CONFLICT,
        NotEnoughBalanceException.class, HttpStatus.BAD_REQUEST,
        TooManyRequestException.class, HttpStatus.TOO_MANY_REQUESTS
    );
    private static final Map<Class<? extends RuntimeException>, String> EXCEPTION_TO_ERROR_CODE = Map.of(
        UserNotFoundException.class, "USER_NOT_FOUND",
        DuplicateIdempotentKeyException.class, "DUPLICATED_IDEMPOTENT_KEY",
        NotEnoughBalanceException.class, "NOT_ENOUGH_BALANCE",
        TooManyRequestException.class, "TOO_MANY_REQUEST"
    );

    @ExceptionHandler
    ResponseEntity<ApiExceptionResponse> handleException(RuntimeException exception){
        HttpStatus httpStatus = EXCEPTION_TO_HTTP_STATUS_CODE.getOrDefault(exception.getClass(), 
                                                                HttpStatus.INTERNAL_SERVER_ERROR);
        String errorCode = EXCEPTION_TO_ERROR_CODE.getOrDefault(exception.getClass(), ERROR_CODE_INTERNAL);

        final ApiExceptionResponse response = ApiExceptionResponse.builder()
                                                .status(httpStatus)
                                                .errorCode(errorCode)
                                                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
