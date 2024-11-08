package shanepark.foodbox.api.exception;

public class ImageParseException extends RuntimeException implements FoodboxException {

    public ImageParseException(Exception e) {
        super(e.getMessage(), e);
    }

    @Override
    public String getErrorCode() {
        return "IMAGE_PARSE_ERROR";
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
