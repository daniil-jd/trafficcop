package ru.inovus.trafficcop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.inovus.trafficcop.entity.NumberEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NumberResponseDto {
    private String number;

    public static NumberResponseDto from(NumberEntity entity) {
        String numbers;
        if (entity.getNumbers() == 0) {
            numbers = "000";
        } else if (entity.getNumbers() >= 10 && entity.getNumbers() < 100) {
            numbers = "0" + String.valueOf(entity.getNumbers());
        } else if (entity.getNumbers() < 10) {
            numbers = "00" + String.valueOf(entity.getNumbers());
        } else {
            numbers = String.valueOf(entity.getNumbers());
        }
        return new NumberResponseDto(
                entity.getFirstLetter() + numbers + entity.getLetters() + " " + entity.getRegion()
        );
    }
}
