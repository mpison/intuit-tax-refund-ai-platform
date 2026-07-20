package com.refundplatform.policy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            PolicyDocumentNotFoundException.class
    )
    ProblemDetail handleNotFound(
            PolicyDocumentNotFoundException exception) {

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND,
                        exception.getMessage()
                );

        problem.setTitle(
                "Policy document not found"
        );

        return problem;
    }

    @ExceptionHandler(
            PolicyStorageException.class
    )
    ProblemDetail handleStorage(
            PolicyStorageException exception) {

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        exception.getMessage()
                );

        problem.setTitle(
                "Policy storage error"
        );

        return problem;
    }

    @ExceptionHandler(
            ResponseStatusException.class
    )
    ProblemDetail handleResponseStatus(
            ResponseStatusException exception) {

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(
                        exception.getStatusCode(),
                        exception.getReason() == null
                                ? "Request failed"
                                : exception.getReason()
                );

        problem.setTitle(
                "Policy request failed"
        );

        return problem;
    }
}
