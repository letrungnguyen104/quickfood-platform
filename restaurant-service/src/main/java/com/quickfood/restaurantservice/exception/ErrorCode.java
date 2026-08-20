package com.quickfood.restaurantservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "SYS_500", "Uncategorized system error"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "SYS_400", "Invalid input data"),
    
    // Restaurant Errors
    RESTAURANT_NOT_FOUND(HttpStatus.NOT_FOUND, "RES_001", "Restaurant not found"),
    INVALID_RESTAURANT_STATUS(HttpStatus.BAD_REQUEST, "RES_002", "Invalid restaurant status");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}