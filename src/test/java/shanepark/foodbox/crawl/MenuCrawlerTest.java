package shanepark.foodbox.crawl;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class MenuCrawlerTest {

    private final MenuCrawler menuCrawler = new MenuCrawler();

    @Test
    void getImage() {
        // Given
        String url = "https://foodboxofficial.modoo.at/?link=e3pla1lv";
        String cssSelector = ".gallery_img img";

        // When
        File file = menuCrawler.getImage(new CrawlConfig(url, cssSelector, 1));

        // Then
        assertThat(file).isNotNull();
        assertThat(file.length()).isGreaterThan(0);
    }

}
