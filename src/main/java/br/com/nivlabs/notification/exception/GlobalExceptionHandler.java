package br.com.nivlabs.notification.exception;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${nivlabs.application.zoneId}")
    public String zoneId;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> methodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                         HttpServletRequest req) {
        return buildResponseEntity(e, HttpStatus.UNPROCESSABLE_ENTITY, getValidations(e), "Unprocessable Entity", req);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                         HttpServletRequest request) {
        return buildResponseEntity(ex, HttpStatus.NOT_FOUND, Collections.emptyList(), "Resource not found", request);
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<StandardError> handleBadRequestException(HttpException ex, HttpServletRequest request) {
        return buildResponseEntity(ex, ex.getStatus(), Collections.emptyList(), ex.getMessage(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponseEntity(ex, HttpStatus.FORBIDDEN, Collections.emptyList(), "Access denied", request);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<StandardError> handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        return buildResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR, Collections.emptyList(), "Null pointer", request);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleException(Exception ex, HttpServletRequest request) {
        return buildResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR, Collections.emptyList(),
                                   HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                   request);
    }

    private List<ErrorItem> getValidations(MethodArgumentNotValidException e) {
        List<ErrorItem> errors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorItem(error.getField(), error.getDefaultMessage()));
        }
        return errors;
    }

    private ResponseEntity<StandardError> buildResponseEntity(Exception ex, HttpStatus status, List<ErrorItem> errors,
                                                              String message, HttpServletRequest req) {
        final Long now = LocalDateTime
                .now()
                .atZone(ZoneId
                        .of(zoneId))
                .toInstant().toEpochMilli();

        StandardError err = new StandardError(
                now,
                status.value(),
                message,
                errors,
                ex.getMessage(),
                req.getRequestURI());

        return new ResponseEntity<StandardError>(err, status);
    }

}
