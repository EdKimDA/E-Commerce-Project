package com.example.kimeduardfinalproject.exceptions;

import com.example.kimeduardfinalproject.dto.responses.KimEduardErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            KimEduardUserNotFoundException.class,
            KimEduardProductNotFoundException.class,
            KimEduardCartNotFoundException.class,
            KimEduardCartItemNotFoundException.class,
            KimEduardOrderNotFoundException.class,
            KimEduardPaymentNotFoundException.class
    })
    public ResponseEntity<KimEduardErrorResponseDTO> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        log.warn("Not found error: {}", ex.getMessage());

        KimEduardErrorResponseDTO response = new KimEduardErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({
            KimEduardDuplicateEmailException.class,
            KimEduardDuplicateUsernameException.class,
            KimEduardDuplicateTransactionException.class
    })
    public ResponseEntity<KimEduardErrorResponseDTO> handleConflict(RuntimeException ex, HttpServletRequest request) {
        log.warn("Conflict error: {}", ex.getMessage());

        KimEduardErrorResponseDTO response = new KimEduardErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler({
            KimEduardInsufficientBalanceException.class,
            KimEduardInsufficientStockException.class,
            KimEduardEmptyCartException.class,
            KimEduardInvalidOrderStatusException.class
    })
    public ResponseEntity<KimEduardErrorResponseDTO> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        log.warn("Bad request error: {}", ex.getMessage());

        KimEduardErrorResponseDTO response = new KimEduardErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(KimEduardAccessDeniedException.class)
    public ResponseEntity<KimEduardErrorResponseDTO> handleAccessDenied(KimEduardAccessDeniedException ex, HttpServletRequest request) {
        log.warn("Access denied: {}", ex.getMessage());

        KimEduardErrorResponseDTO response = new KimEduardErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<KimEduardErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex,
                                                             HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        log.warn("Validation error: {}", errors);

        KimEduardErrorResponseDTO response = new KimEduardErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Request validation failed",
                request.getRequestURI(),
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<KimEduardErrorResponseDTO> handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error", ex);

        KimEduardErrorResponseDTO response = new KimEduardErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Unexpected server error",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
