package com.example.ValidationExmp.controller;


import com.example.ValidationExmp.dto.UserRegisterDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @PostMapping("/register")
    public ResponseEntity<String> register (@Valid @RequestBody UserRegisterDto userRegisterDto){
        return ResponseEntity.ok("user register successfully");
    }
}
