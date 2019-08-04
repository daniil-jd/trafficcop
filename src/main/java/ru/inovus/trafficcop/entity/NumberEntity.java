package ru.inovus.trafficcop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cop_number")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NumberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstLetter;
    private String letters;
    private int numbers;
    private String region;
    private boolean isCurrent;

    public NumberEntity(NumberEntity entity) {
        this.id = 0;
        this.firstLetter = entity.getFirstLetter();
        this.letters = entity.getLetters();
        this.numbers = entity.getNumbers();
        this.region = entity.getRegion();
        this.isCurrent = true;
    }
}
