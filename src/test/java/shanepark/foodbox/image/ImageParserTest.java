package shanepark.foodbox.image;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    private final String TESSDATA_PATH_LINUX = "/usr/share/tesseract-ocr/5/tessdata";
    private final String TESSDATA_PATH_MAC = "/opt/homebrew/share/tessdata";
    private final String TESSDATA_PATH_WIN = "C:\\Program Files\\Tesseract-OCR\\tessdata";
    private final Logger logger = LoggerFactory.getLogger(ImageParserTest.class);

    ImageParser parser;

    @BeforeEach
    void setUp() {
        String dataPath = getDataPathFromOs();
        parser = new ImageParser(dataPath);
    }

    private String getDataPathFromOs() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            return TESSDATA_PATH_MAC;
        }
        if (os.contains("win")) {
            return TESSDATA_PATH_WIN;
        }
        if (os.contains("linux")) {
            return TESSDATA_PATH_LINUX;
        }
        throw new RuntimeException("Unsupported OS");
    }

    @Test
    @DisplayName("Parse image and return parsed menu")
    void parse() throws IOException, TesseractException {
        ClassPathResource resource = new ClassPathResource("메뉴.png");
        File file = resource.getFile();

        List<ParsedMenu> parsedMenu = parser.parse(file);
        logger.info("parsedMenu: \n{}", parsedMenu);
        assertThat(parsedMenu).hasSize(10);
        assertThat(parsedMenu.getFirst().getDate()).isEqualTo("10/28(월)");
    }

    @Test
    @DisplayName("Simply parse image just as text")
    void simpleParse() throws IOException {
        ClassPathResource resource = new ClassPathResource("메뉴.png");
        File file = resource.getFile();

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(getDataPathFromOs());
        tesseract.setLanguage("kor");

        try {
            String result = tesseract.doOCR(file);
            logger.info("result: \n{}", result);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }

}
