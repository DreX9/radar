package com.opporty.radar.handlers;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
            ResponseStatusException ex, HttpServletRequest request) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", ex.getStatusCode().value());
        
        String errorName = "Internal Server Error";
        try {
            errorName = HttpStatus.valueOf(ex.getStatusCode().value()).getReasonPhrase();
        } catch (Exception ignored) {}
        
        body.put("error", errorName);
        body.put("message", ex.getReason());
        body.put("path", request.getRequestURI());
        
        return new ResponseEntity<>(body, ex.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", 400);
        body.put("error", "Bad Request");
        
        // Collect field validation errors
        StringBuilder message = new StringBuilder("Error de validación: ");
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            message.append(error.getField()).append(" ").append(error.getDefaultMessage()).append("; ");
        });
        
        body.put("message", message.toString());
        body.put("path", request.getRequestURI());
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", 400);
        body.put("error", "Bad Request");
        
        String rootMsg = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
        String message = "Error de integridad de datos en la base de datos";
        if (rootMsg != null) {
            if (rootMsg.contains("uq_") || rootMsg.contains("unique") || rootMsg.contains("duplicate key") || rootMsg.contains("duplicada")) {
                message = "El registro ya existe (llave duplicada).";
            } else {
                message = message + ": " + rootMsg;
            }
        }
        
        body.put("message", message);
        body.put("path", request.getRequestURI());
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(
            Exception ex, HttpServletRequest request) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", 500);
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "Ocurrió un error inesperado en el servidor");
        body.put("path", request.getRequestURI());
        
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
