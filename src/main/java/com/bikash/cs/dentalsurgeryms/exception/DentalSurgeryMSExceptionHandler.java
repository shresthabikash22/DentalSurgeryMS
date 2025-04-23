package com.bikash.cs.dentalsurgeryms.exception;

import com.bikash.cs.dentalsurgeryms.exception.general.MethodArgumentNotValidException;
import com.bikash.cs.dentalsurgeryms.exception.role.DuplicateRoleException;
import com.bikash.cs.dentalsurgeryms.exception.role.InvalidRoleException;
import com.bikash.cs.dentalsurgeryms.exception.user.DuplicateUserException;
import com.bikash.cs.dentalsurgeryms.exception.user.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class DentalSurgeryMSExceptionHandler {

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ApiError> handleDuplicateUserException(DuplicateUserException e, HttpServletRequest request){
        ApiError apiError = new ApiError(e.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST.value(), Instant.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request){
        ApiError apiError = new ApiError(e.getMessage(), request.getRequestURI(), HttpStatus.NOT_FOUND.value(), Instant.now());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ApiError> handleInvalidRoleException(InvalidRoleException e, HttpServletRequest request){
        ApiError apiError = new ApiError(e.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST.value(), Instant.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateRoleException.class)
    public ResponseEntity<ApiError> handleDuplicateRoleException(DuplicateRoleException e, HttpServletRequest request) {
        ApiError apiError = new ApiError(e.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST.value(), Instant.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        ApiError apiError = new ApiError(e.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST.value(), Instant.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


}
