package com.quickfood.menuservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "SYS_500", "Uncategorized system error"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "SYS_400", "Invalid input data"),
    
    // Menu Errors
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "MNU_001", "Category not found"),
    MENU_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "MNU_002", "Menu item not found");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}