package shanepark.foodbox.api.domain;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.servlet.NoHandlerFoundException;
import shanepark.foodbox.api.exception.FoodboxException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

public record ApiResponse(
        int status,
        ErrorResponse error,
        Object data
) {
    public static ApiResponse success(MenuResponse menu) {
        return new ApiResponse(OK.value(), null, menu);
    }

    public static ApiResponse success(List<MenuResponse> list) {
        return new ApiResponse(OK.value(), null, list);
    }

    public static ApiResponse fail(Exception e) {
        String errorCode = getErrorCode(e);
        int statusCode = getStatusCode(e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode, e.getMessage());
        return new ApiResponse(statusCode, errorResponse, null);
    }

    private static int getStatusCode(Exception e) {
        if (e instanceof FoodboxException) {
            return ((FoodboxException) e).getStatusCode();
        }
        return generalExceptionStatusCode(e);
    }

    private static String getErrorCode(Exception e) {
        if (e instanceof FoodboxException) {
            return ((FoodboxException) e).getErrorCode();
        }
        return e.getClass().getSimpleName();
    }

    private static int generalExceptionStatusCode(Exception e) {
        if (e instanceof HttpRequestMethodNotSupportedException)
            return METHOD_NOT_ALLOWED.value();

        if (e instanceof HttpMediaTypeNotSupportedException)
            return UNSUPPORTED_MEDIA_TYPE.value();

        if (e instanceof HttpMediaTypeNotAcceptableException)
            return NOT_ACCEPTABLE.value();

        if (e instanceof MissingPathVariableException || e instanceof ConversionNotSupportedException || e instanceof HttpMessageNotWritableException)
            return INTERNAL_SERVER_ERROR.value();

        if (e instanceof TypeMismatchException || e instanceof BindException)
            return BAD_REQUEST.value();

        if (e instanceof NoHandlerFoundException)
            return NOT_FOUND.value();

        return INTERNAL_SERVER_ERROR.value();
    }

}
