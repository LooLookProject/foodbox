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
    @DisplayName("Parse image(nov-11) and return parsed menu")
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
    @DisplayName("tesseract train accuracy test")
    void trainTest() throws IOException {
        ClassPathResource resource = new ClassPathResource("menu-nov-11.png");
        try (InputStream ins = resource.getInputStream()) {
            List<ParsedMenu> parsedMenu = parser.parse(ins);
            logger.info("parsedMenu: \n{}", parsedMenu);

            ParsedMenu menu1112 = parsedMenu.get(1);
            assertThat(menu1112.getMenus().get(5)).isEqualTo("땡초 마늘 멸치 볶음");

            ParsedMenu menu1114 = parsedMenu.get(3);
            assertThat(menu1114.getMenus().get(6)).isEqualTo("양념 깻잎 무침");

            ParsedMenu menu1115 = parsedMenu.get(4);
            assertThat(menu1115.getMenus().get(5)).isEqualTo("매콤 마늘쫑 무침");

            ParsedMenu menu1116 = parsedMenu.get(5);
            assertThat(menu1116.getMenus().get(0)).isEqualTo("찹쌀밥");
            assertThat(menu1116.getMenus().get(4)).isEqualTo("단짠 연근 조림");
            assertThat(menu1116.getMenus().get(6)).isEqualTo("깍두기");

            ParsedMenu menu1122 = parsedMenu.get(9);
            assertThat(menu1122.getMenus().get(8)).isEqualTo("꽃게 짬뽕국");
        }
    }

}
