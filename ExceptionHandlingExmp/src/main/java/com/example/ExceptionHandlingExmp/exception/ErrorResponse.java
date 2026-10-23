package com.example.ExceptionHandlingExmp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

 @Getter
 @Setter 
 @AllArgsConstructor 
 @NoArgsConstructor
public class ErrorResponse {
     
    private String message;
    private String path;
    private int status;
    private LocalDateTime timestamp;


}


