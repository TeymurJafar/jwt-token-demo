package com.example.jwttokendemo.dto.reqDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductReqDto {
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private Integer active;
}
