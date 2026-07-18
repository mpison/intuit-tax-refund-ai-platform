package com.refundplatform.refund.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<Map<String, Object>> handle(ResourceNotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("timestamp", Instant.now().toString(), "errorCode", "RESOURCE_NOT_FOUND", "message", e.getMessage()));
    }
}
