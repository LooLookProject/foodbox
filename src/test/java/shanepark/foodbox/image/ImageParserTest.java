package shanepark.foodbox.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import shanepark.foodbox.image.domain.ParsedMenu;
import shanepark.foodbox.image.service.ImageMarginCalculator;
import shanepark.foodbox.image.service.ImageParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ImageParserTest {
    private final Logger logger = LoggerFactory.getLogger(ImageParserTest.class);
    ImageParser parser = new ImageParser(new ImageMarginCalculator());

    @Test
    @DisplayName("Parse image and return parsed menu")
    void parse() throws IOException {
        ClassPathResource resource = new ClassPathResource("menu-nov-11.png");
        try (InputStream ins = resource.getInputStream()) {
            List<ParsedMenu> parsedMenu = parser.parse(ins);
            logger.info("parsedMenu: \n{}", parsedMenu);

            assertThat(parsedMenu).hasSize(10);
            assertThat(parsedMenu.getFirst().getDate()).matches("\\d{1,2}/\\d{1,2}\\([일월화수목금토]\\)");
        }
    }

    @Test
    @DisplayName("OCT 28 image parse test")
    void parse2() throws IOException {
        ClassPathResource resource = new ClassPathResource("menu-oct-28.png");
        try (InputStream ins = resource.getInputStream()) {
            List<ParsedMenu> parsedMenu = parser.parse(ins);
            logger.info("parsedMenu: \n{}", parsedMenu);

            assertThat(parsedMenu).hasSize(10);
            assertThat(parsedMenu.getFirst().getDate()).matches("\\d{1,2}/\\d{1,2}\\([일월화수목금토]\\)");
        }
    }

    @Test
    @DisplayName("DEC 09 image parse test")
    void parse3() throws IOException {
        ClassPathResource resource = new ClassPathResource("menu-dec-09.png");
        try (InputStream ins = resource.getInputStream()) {
            List<ParsedMenu> parsedMenu = parser.parse(ins);
            logger.info("parsedMenu: \n{}", parsedMenu);

            assertThat(parsedMenu).hasSize(10);
            assertThat(parsedMenu.getFirst().getDate()).matches("\\d{1,2}/\\d{1,2}\\([일월화수목금토]\\)");
        }
    }

    @Test
    @DisplayName("DEC 09 official website image parse test")
    void parse4() throws IOException {
        ClassPathResource resource = new ClassPathResource("menu-dec-09-official.png");
        try (InputStream ins = resource.getInputStream()) {
            List<ParsedMenu> parsedMenu = parser.parse(ins);
            logger.info("parsedMenu: \n{}", parsedMenu);

            assertThat(parsedMenu).hasSize(10);
            assertThat(parsedMenu.getFirst().getDate()).matches("\\d{1,2}/\\d{1,2}\\([일월화수목금토]\\)");
        }
    }

}
