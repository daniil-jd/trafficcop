package ru.inovus.trafficcop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NumberRequestDto {
    private String firstLetter;
    private int numbers;
    private String letters;
}
