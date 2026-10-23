package com.example.ExceptionHandlingExmp.exception;


public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(String message)
    {
        //alttaki yöntem ile mesajı service katmanında da gönderebiliriz

     super("Car not found");
    }
}
