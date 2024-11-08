package shanepark.foodbox.api.domain;

public record ErrorResponse(
        String errorCode,
        String message
) {
}
