package com.quickfood.orderservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // System Errors
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "SYS_500", "Uncategorized system error"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "SYS_400", "Invalid input data");
    
    // Business Errors
    

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}