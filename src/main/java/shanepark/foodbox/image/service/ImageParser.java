package shanepark.foodbox.image.service;

import shanepark.foodbox.image.domain.ParsedMenu;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ImageParser {
    List<ParsedMenu> parse(File file) throws IOException;
}
