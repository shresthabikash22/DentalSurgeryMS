package com.bikash.cs.dentalsurgeryms.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicateResourceException(DuplicateResourceException e, HttpServletRequest request){
        ApiError apiError = new ApiError(e.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST.value(), Instant.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request){
        ApiError apiError = new ApiError(e.getMessage(), request.getRequestURI(), HttpStatus.NOT_FOUND.value(), Instant.now());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ApiError> handleInvalidRoleException(InvalidRoleException e, HttpServletRequest request){
        ApiError apiError = new ApiError(e.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST.value(), Instant.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        ApiError apiError = new ApiError(e.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST.value(), Instant.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ApiError> handleInvalidOperationException(InvalidOperationException e, HttpServletRequest request){
        ApiError apiError = new ApiError(e.getMessage(), request.getRequestURI(), HttpStatus.CONFLICT.value(), Instant.now());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AppointmentNotificationFailedException.class)
    public ResponseEntity<ApiError> handleMessagingException(AppointmentNotificationFailedException e, HttpServletRequest request){
        ApiError apiError = new ApiError(e.getMessage(), request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR.value(), Instant.now());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ADSIllegalStateException.class)
    public ResponseEntity<ApiError> handleADSIllegalStateException(ADSIllegalStateException e, HttpServletRequest request){
        ApiError apiError = new ApiError(e.getMessage(), request.getRequestURI(), HttpStatus.CONFLICT.value(), Instant.now());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AppointmentRequestException.class)
    public ResponseEntity<ApiError> handleAppointmentRequestException(AppointmentRequestException e, HttpServletRequest request){
        ApiError apiError = new ApiError(e.getMessage(), request.getRequestURI(), HttpStatus.CONFLICT.value(), Instant.now());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

}
