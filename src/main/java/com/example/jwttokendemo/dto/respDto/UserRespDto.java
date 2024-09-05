package com.example.jwttokendemo.dto.respDto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserRespDto {

    private Long id;
    private String username;
    private String password;
    private Set<String> roles;

}
