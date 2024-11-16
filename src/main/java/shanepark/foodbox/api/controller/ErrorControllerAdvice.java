package shanepark.foodbox.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import shanepark.foodbox.api.domain.ApiResponse;

@RestControllerAdvice
@Slf4j
public class ErrorControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        if (e instanceof NoResourceFoundException) {
            // ignore favicon error
            return null;
        }
        
        log.error("Error occurred", e);
        return ApiResponse.fail(e);
    }

}
