package shanepark.foodbox.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import shanepark.foodbox.image.domain.ParsedMenu;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ImageParserTest {
    private final Logger logger = LoggerFactory.getLogger(ImageParserTest.class);
    ImageParser parser = new ImageParser();

    @Test
    @DisplayName("Parse image and return parsed menu")
    void parse() throws IOException {
        ClassPathResource resource = new ClassPathResource("메뉴.png");
        try (InputStream ins = resource.getInputStream()) {
            List<ParsedMenu> parsedMenu = parser.parse(ins);
            logger.info("parsedMenu: \n{}", parsedMenu);

            assertThat(parsedMenu).hasSize(10);
            assertThat(parsedMenu.getFirst().getDate()).matches("\\d{1,2}/\\d{1,2}\\([일월화수목금토]\\)");
        }
    }

}
