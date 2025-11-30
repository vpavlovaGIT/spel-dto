package ru.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonRequest {
    private String fullName;
    private Integer minAge;
    private Integer maxAge;
    private String city;
    private String searchExpression;
}
