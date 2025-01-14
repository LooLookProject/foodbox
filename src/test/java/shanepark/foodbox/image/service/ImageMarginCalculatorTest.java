package shanepark.foodbox.image.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import shanepark.foodbox.image.domain.DayRegion;
import shanepark.foodbox.image.ocr.ImageMarginCalculator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ImageMarginCalculatorTest {

    ImageMarginCalculator calculator = new ImageMarginCalculator();

    @Test
    @DisplayName("calc parseRegion return 10 regions")
    void calcParseRegions() throws IOException {
        // Given
        ClassPathResource nov11 = new ClassPathResource("menu/menu-10282024.png");
        BufferedImage image = ImageIO.read(nov11.getInputStream());

        // When
        List<DayRegion> dayRegions = calculator.calcParseRegions(image);

        // Then
        assertThat(dayRegions).hasSize(10);
        DayRegion first = dayRegions.getFirst();
        assertThat(first.date().x()).isEqualTo(128);
        assertThat(first.date().y()).isEqualTo(46);
        assertThat(first.date().width()).isEqualTo(185);
        assertThat(first.date().height()).isEqualTo(47);
    }

}
