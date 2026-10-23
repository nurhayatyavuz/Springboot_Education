package com.example.specificationexmp.exception;

import io.micrometer.core.instrument.binder.okhttp3.OkHttpContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandlerException {

   /*  @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e,ServletWebRequest servletWebRequest) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", e.getMessage());
        map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        map.put("timestamp", LocalDateTime.now());
        map.put("path", servletWebRequest.getRequest().getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);

    }*/

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex,
            ServletWebRequest servletWebRequest) {

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                servletWebRequest.getRequest().getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handeleUserAlreadyExistException(UserAlreadyExistException ex,ServletWebRequest servletWebRequest){
        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setPath(servletWebRequest.getRequest().getRequestURI());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Map<String, Object>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        List<Map<String, Object>> errors = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", fieldError.getDefaultMessage());
            error.put("field", fieldError.getField());
            error.put("timestamp", LocalDateTime.now());
            error.put("code", fieldError.getCode());
            errors.add(error);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(CarAlreadyExistException.class)
    public ResponseEntity<?> handleCarAlreadyExistException(CarAlreadyExistException ex,ServletWebRequest servletWebRequest){
        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setPath(servletWebRequest.getRequest().getRequestURI());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<?> handleCarNotFoundException(CarNotFoundException ex,ServletWebRequest servletWebRequest) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setPath(servletWebRequest.getRequest().getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);


    }



}
