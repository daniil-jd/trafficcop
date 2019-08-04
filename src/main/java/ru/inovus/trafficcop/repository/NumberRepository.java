package ru.inovus.trafficcop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inovus.trafficcop.entity.NumberEntity;

import java.util.Optional;

public interface NumberRepository extends JpaRepository<NumberEntity, Long> {
    Optional<NumberEntity> findByFirstLetterAndNumbersAndLettersAndRegion(String firstLetter, int numbers, String letters, String region);
    Optional<NumberEntity> findByIsCurrentTrue();
}
