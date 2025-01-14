package shanepark.foodbox.image.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import shanepark.foodbox.image.domain.ParsedMenu;
import shanepark.foodbox.image.ocr.ImageMarginCalculator;
import shanepark.foodbox.image.ocr.clova.ImageParserClova;
import shanepark.foodbox.image.ocr.clova.NaverClovaApi;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageParserClovaTest {

    @Mock
    NaverClovaApi naverClovaApi;

    @Test
    void parse() throws IOException {
        // Given
        ImageMarginCalculator imageMarginCalculator = new ImageMarginCalculator();
        ImageParserClova imageParserClova = new ImageParserClova(imageMarginCalculator, naverClovaApi);

        ClassPathResource clovaResponseResource = new ClassPathResource("clova/response.json");
        String clovaResponse = new String(clovaResponseResource.getInputStream().readAllBytes());
        ClassPathResource nov11 = new ClassPathResource("menu/menu-11112024.png");

        // When
        when(naverClovaApi.clovaRequest(anyString())).thenReturn(clovaResponse);
        List<ParsedMenu> parse = imageParserClova.parse(nov11.getFile());

        // Then
        Assertions.assertThat(parse).hasSize(10);
    }

}
