package shanepark.foodbox.image.service;

import shanepark.foodbox.image.domain.ParsedMenu;

import java.io.InputStream;
import java.util.List;

public interface ImageParser {
    List<ParsedMenu> parse(InputStream inputStream);
}
