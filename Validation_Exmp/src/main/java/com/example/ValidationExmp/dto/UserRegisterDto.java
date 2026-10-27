package com.example.ValidationExmp.dto;


import com.example.ValidationExmp.annotation.ConfirmPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfirmPassword
public class UserRegisterDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
}
