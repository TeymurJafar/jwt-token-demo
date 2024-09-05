package com.example.jwttokendemo.dto.respDto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
public class RoleRespDto {
    private Long id;
    private String name;
    private Integer active;
}
