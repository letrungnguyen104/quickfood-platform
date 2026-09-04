package com.quickfood.orderservice.exception;

import com.quickfood.orderservice.dto.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        log.warn("BusinessException: {}", ex.getMessage());
        ErrorCode errorCode = ex.getErrorCode();
        
        ApiResponse<Void> response = ApiResponse.error(errorCode.getCode(), ex.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new java.util.HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        log.warn("ValidationException: {}", errors);
        ApiResponse<Map<String, String>> response = ApiResponse.error(
                ErrorCode.VALIDATION_ERROR.getCode(), 
                ErrorCode.VALIDATION_ERROR.getMessage(), 
                errors
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Internal Server Error: ", ex);
        ApiResponse<Void> response = ApiResponse.error(
                ErrorCode.UNCATEGORIZED_EXCEPTION.getCode(),
                ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage()
        );
        return ResponseEntity.internalServerError().body(response);
    }
}