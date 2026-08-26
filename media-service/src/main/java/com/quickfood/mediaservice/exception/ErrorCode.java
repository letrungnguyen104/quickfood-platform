package com.quickfood.mediaservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "SYS_500", "Uncategorized system error"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "SYS_400", "Invalid input data"),
    
    // Media Errors
    MEDIA_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MEDIA_500", "Media upload failed"),
    FILE_IS_EMPTY(HttpStatus.BAD_REQUEST, "MEDIA_400", "File is empty or not provided");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}