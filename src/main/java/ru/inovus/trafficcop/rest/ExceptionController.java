package ru.inovus.trafficcop.rest;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import ru.inovus.trafficcop.dto.ExceptionResponseDto;
import ru.inovus.trafficcop.exception.NumbersAreOverException;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ExceptionController extends AbstractErrorController {
    private final ErrorAttributes errorAttributes;

    public ExceptionController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping
    public ResponseEntity<ExceptionResponseDto> exceptionHandle(HttpServletRequest request, Locale locale) {
        var webRequest = new ServletWebRequest(request);
        var exception = errorAttributes.getError(webRequest);
        var status = getStatus(request);

        ResponseEntity responseEntity = null;
        if (exception instanceof NumbersAreOverException) {
            responseEntity = ResponseEntity.status(status).body(new ExceptionResponseDto(
                            "NUMBERS_ARE_OVER",
                            "Все номера для данного региона заняты"
                    )
            );
        }
        return responseEntity;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
