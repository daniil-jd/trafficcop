package ru.inovus.trafficcop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.inovus.trafficcop.dto.NumberRequestDto;
import ru.inovus.trafficcop.entity.NumberEntity;
import ru.inovus.trafficcop.exception.NumbersAreOverException;
import ru.inovus.trafficcop.repository.NumberRepository;

import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class NumberService {
    private final NumberRepository numberRepository;
    private final Random random = new Random();

    private final String[] availableLetters = new String[]{"А", "В", "Е", "К", "М", "Н", "О", "Р", "С", "Т", "У", "Х"};
    private final String region = "116 RUS";

    public NumberEntity getNextNumber() {
        var current = numberRepository.findByIsCurrentTrue();
        if (current.isPresent()) {
            findAndDisableCurrentEntity();
            return generateNextNumberEntity(current.get());
        } else {
            return getNewRandomNumber();
        }
    }

    public NumberEntity getNewRandomNumber() {
        NumberEntity entityCandidate = null;
        boolean isEntityNew = false;
        int count = 0;
        while (!isEntityNew) {
            if (++count >= 12 * 10 * 10 * 10 * 12 * 12) {
                throw new NumbersAreOverException();
            }

            entityCandidate = generateRandNumberEntity();
            var optionalNumberEntity = numberRepository.findByFirstLetterAndNumbersAndLettersAndRegion(
                    entityCandidate.getFirstLetter(),
                    entityCandidate.getNumbers(),
                    entityCandidate.getLetters(),
                    entityCandidate.getRegion()
            );
            if (!optionalNumberEntity.isPresent()) {
                isEntityNew = true;

                findAndDisableCurrentEntity();
                numberRepository.save(entityCandidate);
            }
        }

        return entityCandidate;
    }

    private void findAndDisableCurrentEntity() {
        var current = numberRepository.findByIsCurrentTrue();
        if (current.isPresent()) {
            var entity = current.get();
            entity.setCurrent(false);
            numberRepository.save(entity);
        }
    }

    private NumberEntity generateNextNumberEntity(NumberEntity currentEntity) {
        var entity = new NumberEntity(currentEntity);
        if (entity.getNumbers() == 1000 - 1) {
            entity.setNumbers(0);

            entity = getNextLetters(entity);
        } else {
            entity.setNumbers(entity.getNumbers() + 1);
        }

        var optionalEntity = numberRepository.findByFirstLetterAndNumbersAndLettersAndRegion(
                entity.getFirstLetter(),
                entity.getNumbers(),
                entity.getLetters(),
                entity.getRegion()
        );
        if (!optionalEntity.isPresent()) {
            numberRepository.save(entity);
        }

        return entity;
    }

    private NumberEntity getNextLetters(NumberEntity entity) {
        var letters = entity.getFirstLetter() + entity.getLetters();
        var result = getNextLettersRecursively(letters);

        return new NumberEntity(
                0,
                result.substring(0, result.length() - 2),
                result.substring(1),
                entity.getNumbers(),
                entity.getRegion(),
                true
        );
    }

    private String getNextLettersRecursively(String letters) {
        if (letters.length() < 1) {
            throw new NumbersAreOverException();
        }

        String lastLetter = letters.substring(letters.length() - 1);
        String othersLetters = letters.substring(0, letters.length() - 1);

        int letterNumber = getLetterNumberInArray(lastLetter);
        if (letterNumber == availableLetters.length - 1) {
            lastLetter = availableLetters[0];

            othersLetters = getNextLettersRecursively(othersLetters);
        } else {
            lastLetter = availableLetters[letterNumber + 1];
        }
        return othersLetters + lastLetter;
    }

    private int getLetterNumberInArray(String letter) {
        for (int i = 0; i < availableLetters.length; i++) {
            if (availableLetters[i].equals(letter)) {
                return i;
            }
        }
        return 0;
    }

    private NumberEntity generateRandNumberEntity() {
        return new NumberEntity(
                0,
                getRandFirstLetter(),
                getRandLetters(),
                getRandNumbers(),
                region,
                true
        );
    }

    private String getRandFirstLetter() {
        int rand = random.nextInt(availableLetters.length -1);
        return availableLetters[rand];
    }

    private String getRandLetters() {
        return availableLetters[random.nextInt(availableLetters.length -1)]
                + availableLetters[random.nextInt(availableLetters.length -1)];
    }

    private int getRandNumbers() {
        int rand = random.nextInt(1000 - 100 + 1);
        if (rand == 1000) {
            return 0;
        }
        return rand;
    }

    public NumberEntity setCurrentNumberEntity(NumberRequestDto requestDto) {
        var newEntity = new NumberEntity(
                0,
                requestDto.getFirstLetter(),
                requestDto.getLetters(),
                requestDto.getNumbers(),
                region,
                true
        );
        findAndDisableCurrentEntity();
        numberRepository.save(newEntity);
        return newEntity;
    }
}
