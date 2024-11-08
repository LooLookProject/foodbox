package shanepark.foodbox.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shanepark.foodbox.api.domain.ApiResponse;

@RestControllerAdvice
@Slf4j
public class ErrorControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        log.error("Error occurred", e);
        return ApiResponse.fail(e);
    }

}
