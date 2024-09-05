package com.example.jwttokendemo.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum StatusEnum {
    ACTIVE(1),
    DEACTIVATE(0);

    public Integer value;
}
