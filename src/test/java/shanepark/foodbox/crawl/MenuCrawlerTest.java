package shanepark.foodbox.crawl;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class MenuCrawlerTest {

    private final MenuCrawler menuCrawler = new MenuCrawler();

    @Test
    void getImage() throws IOException {
        // Given
        String url = "https://foodboxofficial.modoo.at/?link=e3pla1lv";
        String cssSelector = ".gallery_img img";

        // When
        InputStream inputStream = menuCrawler.getImage(new CrawlConfig(url, cssSelector, 1));

        // Then
        assertThat(inputStream).isNotNull();
        assertThat(inputStream.available()).isGreaterThan(0);
        assertThat(inputStream.read()).isNotEqualTo(-1);
    }

}
