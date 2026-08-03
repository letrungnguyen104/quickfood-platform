package com.quickfood.authservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // System Errors
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "SYS_500", "Uncategorized system error"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "SYS_400", "Invalid input data"),
    
    // Business Errors
    USER_EXISTED(HttpStatus.BAD_REQUEST, "AUTH_001", "Username already exists"),
    EMAIL_EXISTED(HttpStatus.BAD_REQUEST, "AUTH_002", "Email already in use"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_003", "User not found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH_004", "Invalid password"),
    
    // JWT Errors
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "AUTH_005", "Unauthenticated"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_006", "Token has expired"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH_007", "Invalid or tampered token");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}