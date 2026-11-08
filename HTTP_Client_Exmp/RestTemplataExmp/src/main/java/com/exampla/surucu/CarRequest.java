package com.exampla.surucu;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private String marka;
    @NotBlank(message="model boş olamaz")
    @Size(min = 3,max = 25,message = "karakter sınırı yetersiz veya aşıldı")
    private String model;
    private Integer fiyat;
    @NotNull(message="yılı bos olamaz ")
    @Max(value=10,message="10 yılı geçmemeli")
    private Integer yil;
}
