package com.bikash.cs.dentalsurgeryms.exception;

import com.bikash.cs.dentalsurgeryms.exception.general.DuplicateResourceException;
import com.bikash.cs.dentalsurgeryms.exception.general.MethodArgumentNotValidException;
import com.bikash.cs.dentalsurgeryms.exception.general.ResourceNotFoundException;
import com.bikash.cs.dentalsurgeryms.exception.role.InvalidRoleException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class DentalSurgeryMSExceptionHandler {

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


}
