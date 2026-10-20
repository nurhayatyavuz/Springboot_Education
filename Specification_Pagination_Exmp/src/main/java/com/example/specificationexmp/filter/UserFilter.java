package com.example.specificationexmp.filter;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.aspectj.bridge.IMessage;

@Data
public class UserFilter {


    @NotBlank(message = "İsim boş olamaz")
    private String name;

    @NotBlank(message = "Soyisim boş olamaz")
    private String surname;

    @NotNull(message = "Yaş boş olamaz")
    @Min(value = 0, message = "Yaş 0'dan küçük olamaz")
    private Integer age;

    @NotBlank(message = "Şehir boş olamaz")
    private String city;

    @NotNull(message = "Aktiflik durumu boş olamaz")
    private Boolean active;
}
