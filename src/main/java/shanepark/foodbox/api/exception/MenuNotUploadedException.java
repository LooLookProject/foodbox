package shanepark.foodbox.api.exception;

public class MenuNotUploadedException extends RuntimeException implements FoodboxException {

    public MenuNotUploadedException() {
        super("Today Menu is not uploaded yet");
    }

    @Override
    public String getErrorCode() {
        return "MENU_NOT_UPLOADED";
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

}
