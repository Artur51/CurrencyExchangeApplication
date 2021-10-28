package com.currency.server.controllers.errors;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.currency.server.utils.Utils;

@ControllerAdvice
public class ValidationExceprionsHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request) {
        String message = ex.getMessage();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleBadRequestException(
            ResponseStatusException ex) {
        final String message = ex.getMessage();
        return new ResponseEntity<>(message, ex.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errors = Utils.collectErrors(bindingResult);
        Map<String, Object> body = createErrorBody(status, errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        String name = ex.getParameterName();
        List<String> errors = Collections.singletonList("Missing parameter " + name);
        Map<String, Object> body = createErrorBody(status, errors);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Map<String, Object> createErrorBody(HttpStatus status, List<String> errors) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("errors", errors);
        return body;
    }
}