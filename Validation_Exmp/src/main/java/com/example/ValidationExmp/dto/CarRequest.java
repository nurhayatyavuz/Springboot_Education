package com.example.ValidationExmp.dto;

import com.example.ValidationExmp.annotation.NotAdmin;
import com.example.ValidationExmp.annotation.NotCiftSayi;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarRequest {


    @Size(min=3,max=20)
    @NotAdmin
    private String marka;
    @NotBlank(message="model boş olamaz")
    @Size(min = 3,max = 25,message = "karakter sınırı yetersiz veya aşıldı")
    private String model;
    @NotCiftSayi
    private Integer fiyat;
    @NotNull(message="yılı bos olamaz ")
    @Max(value=10,message="10 yılı geçmemeli")
    private Integer yil;
}
