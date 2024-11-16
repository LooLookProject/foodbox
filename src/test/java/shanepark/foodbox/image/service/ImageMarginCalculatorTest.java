package shanepark.foodbox.image.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import shanepark.foodbox.image.domain.ImageMarginData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class ImageMarginCalculatorTest {

    ImageMarginCalculator calculator = new ImageMarginCalculator();

    @Test
    @DisplayName("Calculate margin data from image menu-nov-11.png")
    void calcMargin1() throws IOException {
        // Given
        ClassPathResource nov11 = new ClassPathResource("menu-nov-11.png");
        BufferedImage image = ImageIO.read(nov11.getInputStream());

        // When
        ImageMarginData data = calculator.calcMargin(image);

        // Then
        assertThat(data.singleHeight()).isEqualTo(351);
        assertThat(data.headerHeight()).isEqualTo(47);
        assertThat(data.gapSmall()).isEqualTo(3);
        assertThat(data.marginLeft()).isEqualTo(118);
        assertThat(data.marginTop()).isEqualTo(34);
        assertThat(data.gapBig()).isEqualTo(31);
        assertThat(data.singleWidth()).isEqualTo(188);
    }

    @Test
    @DisplayName("Calculate margin data from image menu-oct-28.png")
    void calcMargin2() throws IOException {
        // Given
        ClassPathResource nov11 = new ClassPathResource("menu-oct-28.png");
        BufferedImage image = ImageIO.read(nov11.getInputStream());

        // When
        ImageMarginData data = calculator.calcMargin(image);

        // Then
        assertThat(data.singleHeight()).isEqualTo(344);
        assertThat(data.headerHeight()).isEqualTo(47);
        assertThat(data.gapSmall()).isEqualTo(2);
        assertThat(data.marginLeft()).isEqualTo(128);
        assertThat(data.marginTop()).isEqualTo(46);
        assertThat(data.gapBig()).isEqualTo(30);
        assertThat(data.singleWidth()).isEqualTo(185);
    }

}
