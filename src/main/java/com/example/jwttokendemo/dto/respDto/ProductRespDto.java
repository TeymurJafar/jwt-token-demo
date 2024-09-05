package com.example.jwttokendemo.dto.respDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRespDto {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private Integer active;
}
