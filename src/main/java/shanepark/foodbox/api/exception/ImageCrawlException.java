package shanepark.foodbox.api.exception;

import java.io.IOException;

public class ImageCrawlException extends RuntimeException {

    public ImageCrawlException(IOException e) {
        super(e.getMessage(), e);
    }
}
