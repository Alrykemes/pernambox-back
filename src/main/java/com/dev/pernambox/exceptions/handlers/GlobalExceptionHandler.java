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
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Type", ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation Error");

        log.error("Validation Error: {}", message);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", message, request);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthorizationException(AuthorizationException ex, HttpServletRequest request) {
        log.error("Authorization Error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Authorization Error", ex.getMessage(), request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        log.error("Authentication Error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication Error", ex.getMessage(), request);
    }

    @ExceptionHandler(PasswordResetException.class)
    public ResponseEntity<ErrorResponseDto> handlePasswordResetException(PasswordResetException ex, HttpServletRequest request) {
        log.error("Password Reset Error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Password Reset Error", ex.getMessage(), request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        log.error("Not Found in DB: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found Error", ex.getMessage(), request);
    }

    @ExceptionHandler(CreateEntityException.class)
    public ResponseEntity<ErrorResponseDto> handleCreateEntityException(NotFoundException ex, HttpServletRequest request) {
        log.error("Error to create in DB: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Create Entity error", ex.getMessage(), request);
    }

    @ExceptionHandler(UpdateEntityException.class)
    public ResponseEntity<ErrorResponseDto> handleUpdateEntityException(NotFoundException ex, HttpServletRequest request) {
        log.error("Error to update in DB: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Update Entity Error", ex.getMessage(), request);
    }

    @ExceptionHandler(DeleteEntityException.class)
    public ResponseEntity<ErrorResponseDto> handleDeleteEntityException(NotFoundException ex, HttpServletRequest request) {
        log.error("Error to delete in DB: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Delete Entity Error", ex.getMessage(), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error("Body error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Body error", ex.getMessage(), request);
    }

    @ExceptionHandler(MinioException.class)
    public ResponseEntity<ErrorResponseDto> handleMinioException(MinioException ex, HttpServletRequest request) {
        log.error("Minio Error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Error to upload Files", ex.getMessage(), request);
    }

    @ExceptionHandler(UploadFilesException.class)
    public ResponseEntity<ErrorResponseDto> handleUploadFilesException(UploadFilesException ex, HttpServletRequest request) {
        log.error("Upload Files Error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Error to upload Files", ex.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("Violação de integridade de dados: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Erro ao criar/atualizar dados", ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(HttpStatus status, String title, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(new ErrorResponseDto(status.value(), title, message, request.getRequestURI(), LocalDateTime.now()));
    }
}