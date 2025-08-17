package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        logger.warn("Validation error on {}: {}", request.getRequestURI(), ex.getMessage());
        
        List<ApiResponse.ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiResponse.ValidationError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("Validation failed")
                .errorCode("VALIDATION_ERROR")
                .validationErrors(validationErrors)
                .build();
        
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        logger.warn("Constraint violation on {}: {}", request.getRequestURI(), ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error("Constraint violation: " + ex.getMessage(), "CONSTRAINT_VIOLATION");
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<ApiResponse<Object>> handlePaymentProcessingException(PaymentProcessingException ex, HttpServletRequest request) {
        logger.error("Payment processing error on {}: {} [{}]", request.getRequestURI(), ex.getMessage(), ex.getErrorCode());
        
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage(), ex.getErrorCode());
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        logger.error("Business logic error on {}: {} [{}]", request.getRequestURI(), ex.getMessage(), ex.getErrorCode());
        
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage(), ex.getErrorCode());
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        logger.warn("Resource not found on {}: {}", request.getRequestURI(), ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage(), "RESOURCE_NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(com.example.demo.exception.AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomAuthenticationException(com.example.demo.exception.AuthenticationException ex, HttpServletRequest request) {
        logger.warn("Authentication failed on {}: {}", request.getRequestURI(), ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage(), "AUTHENTICATION_FAILED");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomAuthorizationException(AuthorizationException ex, HttpServletRequest request) {
        logger.warn("Authorization failed on {}: {}", request.getRequestURI(), ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage(), "ACCESS_DENIED");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleTokenValidation(TokenValidationException ex, HttpServletRequest request) {
        logger.warn("Token validation failed on {}: {}", request.getRequestURI(), ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage(), "TOKEN_VALIDATION_FAILED");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        logger.warn("Malformed JSON request on {}: {}", request.getRequestURI(), ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error("Malformed JSON request", "MALFORMED_JSON");
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        logger.warn("Type mismatch on {}: {}", request.getRequestURI(), ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error("Invalid parameter type: " + ex.getName(), "TYPE_MISMATCH");
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        logger.warn("Missing parameter on {}: {}", request.getRequestURI(), ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error("Missing required parameter: " + ex.getParameterName(), "MISSING_PARAMETER");
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        logger.warn("Method not supported on {}: {}", request.getRequestURI(), ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error("HTTP method not supported: " + ex.getMethod(), "METHOD_NOT_SUPPORTED");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {
        logger.warn("No handler found for {}: {}", request.getRequestURI(), ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error("Endpoint not found", "ENDPOINT_NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataAccessException(DataAccessException ex, HttpServletRequest request) {
        logger.error("Database error on {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        
        ApiResponse<Object> response = ApiResponse.error("Database operation failed", "DATABASE_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        logger.warn("Illegal argument on {}: {}", request.getRequestURI(), ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage(), "ILLEGAL_ARGUMENT");
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error on {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        
        ApiResponse<Object> response = ApiResponse.error("An unexpected error occurred", "INTERNAL_SERVER_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
