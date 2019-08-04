package ru.inovus.trafficcop.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.inovus.trafficcop.dto.NumberRequestDto;
import ru.inovus.trafficcop.dto.NumberResponseDto;
import ru.inovus.trafficcop.service.NumberService;

@RestController
@RequestMapping("/number")
@RequiredArgsConstructor
public class NumberController {
    private final NumberService numberService;

    @GetMapping("/random")
    public NumberResponseDto getRandomNumber() {
        return NumberResponseDto.from(
                numberService.getNewRandomNumber()
        );
    }

    @GetMapping("/next")
    public NumberResponseDto getNextNumber() {
        return NumberResponseDto.from(
                numberService.getNextNumber()
        );
    }

    /**
     * An additional method whose purpose is to be able to set the desired number.
     * @param requestDto desired number
     * @return number dto
     */
    @PostMapping("/set")
    public NumberResponseDto setNumber(@RequestBody NumberRequestDto requestDto) {
        return NumberResponseDto.from(
                numberService.setCurrentNumberEntity(requestDto)
        );
    }

}
