package shanepark.foodbox.api.domain;

import java.time.LocalDate;
import java.util.List;

public record MenuResponse(
        LocalDate date,
        List<String> menus
) {

}
