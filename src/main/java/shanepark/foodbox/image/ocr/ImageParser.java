package shanepark.foodbox.image.ocr;

import shanepark.foodbox.image.domain.ParsedMenu;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ImageParser {
    List<ParsedMenu> parse(File file) throws IOException;
}
