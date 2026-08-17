package com.quickfood.userservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "SYS_500", "Uncategorized system error"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "SYS_400", "Invalid input data"),
    
    // User Errors
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "User profile not found"),
    PHONE_NUMBER_EXISTED(HttpStatus.BAD_REQUEST, "USER_002", "Phone number already in use"),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER_003", "User profile with this email already exists"),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_004", "Address not found");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}