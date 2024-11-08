package shanepark.foodbox.api.exception;

public class ImageParseException extends RuntimeException {

    public ImageParseException(Exception e) {
        super(e.getMessage(), e);
    }
}
