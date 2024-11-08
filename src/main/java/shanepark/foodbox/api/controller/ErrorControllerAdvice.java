package shanepark.foodbox.api.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shanepark.foodbox.api.domain.ApiResponse;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        return ApiResponse.fail(e);
    }

}
