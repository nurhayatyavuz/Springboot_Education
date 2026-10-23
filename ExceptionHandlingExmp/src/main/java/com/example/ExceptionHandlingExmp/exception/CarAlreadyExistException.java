package com.example.ExceptionHandlingExmp.exception;

public class CarAlreadyExistException extends RuntimeException{
    public CarAlreadyExistException(String message){
        super("Car Already Exist ");
    }
}
