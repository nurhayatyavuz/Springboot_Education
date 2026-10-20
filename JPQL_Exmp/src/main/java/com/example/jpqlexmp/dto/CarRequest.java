package com.example.jpqlexmp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarRequest {
    private String marka;
    private String model;
    private Long fiyat;
    private Integer yas;
}
