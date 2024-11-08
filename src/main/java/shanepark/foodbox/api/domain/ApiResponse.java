package shanepark.foodbox.api.domain;

import java.util.List;

public record ApiResponse(
        ErrorResponse error,
        Object data
) {
    public static ApiResponse success(MenuResponse menu) {
        return new ApiResponse(null, menu);
    }

    public static ApiResponse success(List<MenuResponse> list) {
        return new ApiResponse(null, list);
    }
}
