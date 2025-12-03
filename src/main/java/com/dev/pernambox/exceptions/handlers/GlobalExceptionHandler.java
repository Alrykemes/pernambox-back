package com.dev.pernambox.exceptions.handlers;

import com.dev.pernambox.exceptions.*;
import com.dev.pernambox.exceptions.dtos.ErrorResponseDto;
import io.minio.errors.MinioException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralException(Exception ex, HttpServletRequest request) {
        StackTraceElement origin = ex.getStackTrace()[0];
        log.error("Exception Não Tratada: {}, \n messagem: {} \n Classe: {} \n Método: {} \n Linha: {}",
                ex.getClass(), ex.getMessage(), origin.getClass(), origin.getMethodName(), origin.getLineNumber());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.error("Type mismatch: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Parameter Type",
                "A parameter has an invalid type.", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation Error");

        log.error("Validation error: {}", message);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", message, request);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthorizationException(AuthorizationException ex, HttpServletRequest request) {
        log.error("Authorization error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Authorization Error",
                ex.getMessage(), request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        log.error("Authentication error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication Error",
                ex.getMessage(), request);
    }

    @ExceptionHandler(PasswordResetException.class)
    public ResponseEntity<ErrorResponseDto> handlePasswordResetException(PasswordResetException ex, HttpServletRequest request) {
        log.error("Password reset error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Password Reset Error",
                ex.getMessage(), request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        log.error("Not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request);
    }

    @ExceptionHandler(CreateEntityException.class)
    public ResponseEntity<ErrorResponseDto> handleCreateEntityException(CreateEntityException ex, HttpServletRequest request) {
        log.error("Create entity error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Create Entity Error",
                ex.getMessage(), request);
    }

    @ExceptionHandler(UpdateEntityException.class)
    public ResponseEntity<ErrorResponseDto> handleUpdateEntityException(UpdateEntityException ex, HttpServletRequest request) {
        log.error("Update entity error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Update Entity Error",
                ex.getMessage(), request);
    }

    @ExceptionHandler(DeleteEntityException.class)
    public ResponseEntity<ErrorResponseDto> handleDeleteEntityException(DeleteEntityException ex, HttpServletRequest request) {
        log.error("Delete entity error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Delete Entity Error",
                ex.getMessage(), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error("Unreadable body: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Malformed JSON Body",
                "The request body is invalid or malformed.", request);
    }

    @ExceptionHandler(MinioException.class)
    public ResponseEntity<ErrorResponseDto> handleMinioException(MinioException ex, HttpServletRequest request) {
        log.error("MinIO error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "File Upload Error",
                "Error uploading file to storage.", request);
    }

    @ExceptionHandler(UploadFilesException.class)
    public ResponseEntity<ErrorResponseDto> handleUploadFilesException(UploadFilesException ex, HttpServletRequest request) {
        log.error("Upload error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "File Upload Error",
                ex.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("Data integrity violation: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, "Data Integrity Error",
                "The operation violates database constraints.", request);
    }

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(HttpStatus status, String title, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(
                new ErrorResponseDto(status.value(), title, message,
                        request.getRequestURI(), LocalDateTime.now())
        );
    }
}
