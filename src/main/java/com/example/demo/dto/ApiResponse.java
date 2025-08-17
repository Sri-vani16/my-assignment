package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    private LocalDateTime timestamp;
    private String requestId;
    private PaginationInfo pagination;
    private List<ValidationError> validationErrors;

    private ApiResponse() {
        this.timestamp = LocalDateTime.now();
        this.requestId = UUID.randomUUID().toString();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message("Operation completed successfully")
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .build();
    }

    public static class Builder<T> {
        private final ApiResponse<T> response;

        public Builder() {
            this.response = new ApiResponse<>();
        }

        public Builder<T> success(boolean success) {
            response.success = success;
            return this;
        }

        public Builder<T> message(String message) {
            response.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            response.data = data;
            return this;
        }

        public Builder<T> errorCode(String errorCode) {
            response.errorCode = errorCode;
            return this;
        }

        public Builder<T> requestId(String requestId) {
            response.requestId = requestId;
            return this;
        }

        public Builder<T> pagination(PaginationInfo pagination) {
            response.pagination = pagination;
            return this;
        }

        public Builder<T> validationErrors(List<ValidationError> validationErrors) {
            response.validationErrors = validationErrors;
            return this;
        }

        public ApiResponse<T> build() {
            return response;
        }
    }

    public static class PaginationInfo {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean hasNext;
        private boolean hasPrevious;

        public PaginationInfo(int page, int size, long totalElements, int totalPages) {
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.hasNext = page < totalPages - 1;
            this.hasPrevious = page > 0;
        }

        public int getPage() { return page; }
        public int getSize() { return size; }
        public long getTotalElements() { return totalElements; }
        public int getTotalPages() { return totalPages; }
        public boolean isHasNext() { return hasNext; }
        public boolean isHasPrevious() { return hasPrevious; }
    }

    public static class ValidationError {
        private String field;
        private String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() { return field; }
        public String getMessage() { return message; }
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public String getErrorCode() { return errorCode; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getRequestId() { return requestId; }
    public PaginationInfo getPagination() { return pagination; }
    public List<ValidationError> getValidationErrors() { return validationErrors; }
}