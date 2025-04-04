/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.borak.cwb.backend.exceptions.handler;

import com.borak.cwb.backend.exceptions.DatabaseException;
import com.borak.cwb.backend.exceptions.DuplicateResourceException;
import com.borak.cwb.backend.exceptions.EmailTakenException;
import com.borak.cwb.backend.exceptions.InvalidInputException;
import com.borak.cwb.backend.exceptions.ProfileNameTakenException;
import com.borak.cwb.backend.exceptions.ResourceNotFoundException;
import com.borak.cwb.backend.exceptions.UnexpectedException;
import com.borak.cwb.backend.exceptions.UsernameTakenException;
import com.borak.cwb.backend.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 *
 * @author Mr. Poyo
 */
@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetail.setTitle("Resource Not Found");
        errorDetail.setDetails(new String[]{ex.getMessage()});
        errorDetail.setDeveloperMessage(ex.getClass().getName());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResourceException(DuplicateResourceException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.CONFLICT.value());
        errorDetail.setTitle("Duplicate resource");
        errorDetail.setDetails(new String[]{ex.getMessage()});
        errorDetail.setDeveloperMessage(ex.getClass().getName());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InvalidInputException.class)
    public ResponseEntity<?> handleInvalidInputException(InvalidInputException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle("Bad Request");
        errorDetail.setDetails(new String[]{ex.getMessage()});
        errorDetail.setDeveloperMessage(ex.getClass().getName());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
        UsernameTakenException.class,
        EmailTakenException.class,
        ProfileNameTakenException.class
    })
    public ResponseEntity<?> handleUserAuthExceptions(RuntimeException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle("Bad Request");
        errorDetail.setDetails(new String[]{ex.getMessage()});
        errorDetail.setDeveloperMessage(ex.getClass().getName());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle("Bad Request");
        List<String> list = new ArrayList(ex.getConstraintViolations().size());
        ex.getConstraintViolations().forEach((error) -> {
            list.add(error.getMessage());
        });
        errorDetail.setDetails(list.toArray(String[]::new));
        errorDetail.setDeveloperMessage(ex.getClass().getName());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle("Bad Request");
        errorDetail.setDetails(ex.getMessages());
        errorDetail.setDeveloperMessage(ex.getClass().getName());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle("Bad Request");
        errorDetail.setDetails(new String[]{ex.getMessage()});
        errorDetail.setDeveloperMessage(ex.getClass().getName());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, NullPointerException.class, UnexpectedException.class})
    public ResponseEntity<?> handleIllegalArgumentException(RuntimeException ex, HttpServletRequest request) {
        log.error(ex.getMessage());
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetail.setTitle("Internal server error");
        errorDetail.setDetails(new String[]{"Unexpected error"});
        errorDetail.setDeveloperMessage(ex.getClass().getName());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {
        DatabaseException.class
    })
    public ResponseEntity<?> handleUnexpectedExceptions(DatabaseException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetail.setTitle("Unexpected error");
        errorDetail.setDetails(new String[]{ex.getMessage()});
        errorDetail.setDeveloperMessage(ex.getClass().getName());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
