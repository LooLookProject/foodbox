package shanepark.foodbox.image;

import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import shanepark.foodbox.image.domain.ParsedMenu;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ImageParserTest {
    private final String dataPath = "/usr/share/tesseract-ocr/5/tessdata";
    private final Logger logger = LoggerFactory.getLogger(ImageParserTest.class);

    ImageParser parser = new ImageParser(dataPath);

    @Test
    void parse() throws IOException, TesseractException {
        ClassPathResource resource = new ClassPathResource("메뉴.png");
        File file = resource.getFile();

        List<ParsedMenu> parsedMenu = parser.parse(file);
        logger.info("parsedMenu: {}", parsedMenu);
        assertThat(parsedMenu).hasSize(10);
        assertThat(parsedMenu.getFirst().getDate()).isEqualTo("10/28(월)");
    }
}
