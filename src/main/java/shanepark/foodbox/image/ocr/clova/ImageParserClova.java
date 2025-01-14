package shanepark.foodbox.image.ocr.clova;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shanepark.foodbox.image.domain.DayRegion;
import shanepark.foodbox.image.domain.ParsedMenu;
import shanepark.foodbox.image.ocr.ImageMarginCalculator;
import shanepark.foodbox.image.ocr.ImageParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import static org.apache.commons.io.FileUtils.readFileToByteArray;

@RequiredArgsConstructor
@Component
public class ImageParserClova implements ImageParser {
    private final ImageMarginCalculator imageMarginCalculator;
    private final NaverClovaApi naverClovaApi;
    private final Base64.Encoder encoder = Base64.getEncoder();

    @Override
    public List<ParsedMenu> parse(File file) throws IOException {
        String base64 = encoder.encodeToString(readFileToByteArray(file));
        String response = naverClovaApi.clovaRequest(base64);

        BufferedImage bufferedImage = ImageIO.read(file);
        List<DayRegion> dayRegions = imageMarginCalculator.calcParseRegions(bufferedImage);
        return parseResponse(response, dayRegions);
    }

    // TODO: Implement response parsing
    private List<ParsedMenu> parseResponse(String response, List<DayRegion> dayRegions) {
        return List.of();
    }

}
