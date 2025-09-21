package com.armemius.labwork.controller;

import com.armemius.labwork.exceptions.LabworkException;
import com.armemius.labwork.exceptions.auth.UserNotFoundException;
import com.armemius.labwork.exceptions.auth.UsernameTakenException;
import com.armemius.labwork.exceptions.object.AddressNotFoundException;
import com.armemius.labwork.exceptions.object.CoordinatesNotFoundException;
import com.armemius.labwork.exceptions.object.LocationNotFoundException;
import com.armemius.labwork.exceptions.object.ObjectNotFoundException;
import com.armemius.labwork.exceptions.object.OrganizationNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> createBody(HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        return body;
    }

    // Custom business exceptions
    @ExceptionHandler(UsernameTakenException.class)
    public ResponseEntity<Object> handleUsernameTaken(UsernameTakenException ex) {
        Map<String, Object> body = createBody(HttpStatus.CONFLICT);
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            UsernameNotFoundException.class,
            UserNotFoundException.class,
            PropertyReferenceException.class,
            ObjectNotFoundException.class,
    })
    public ResponseEntity<Object> handleNotFound(Exception ex) {
        Map<String, Object> body = createBody(HttpStatus.NOT_FOUND);
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex) {
        Map<String, Object> body = createBody(HttpStatus.UNAUTHORIZED);
        body.put("error", "Invalid credentials");
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    // Spring MVC / default errors
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNotFound(NoHandlerFoundException ex) {
        Map<String, Object> body = createBody(HttpStatus.NOT_FOUND);
        body.put("error", "Endpoint not found");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNotFound(NoResourceFoundException ex) {
        Map<String, Object> body = createBody(HttpStatus.NOT_FOUND);
        body.put("error", "Endpoint not found");
        body.put("endpoint", ex.getResourcePath());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        Map<String, Object> body = createBody(HttpStatus.METHOD_NOT_ALLOWED);
        body.put("error", ex.getMessage());
        body.put("method", ex.getMethod());
        body.put("allowed", ex.getSupportedMethods());
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> body = createBody(HttpStatus.BAD_REQUEST);
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> body = createBody(HttpStatus.BAD_REQUEST);
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String field = violation.getPropertyPath().toString();
            errors.put(field, violation.getMessage());
        }
        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Fallback for other exceptions
    @ExceptionHandler(LabworkException.class)
    public ResponseEntity<Object> handleCustomExceptions(LabworkException ex) {
        Map<String, Object> body = createBody(HttpStatus.BAD_REQUEST);
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOtherExceptions(Exception ex) {
        Map<String, Object> body = createBody(HttpStatus.INTERNAL_SERVER_ERROR);
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
