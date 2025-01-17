package shanepark.foodbox.crawl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import shanepark.foodbox.api.exception.ImageCrawlException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Component
@Slf4j
public class MenuCrawler {

    public Path getImage(CrawlConfig crawlConfig) {
        try {
            String url = crawlConfig.getCrawlUrl();
            int index = crawlConfig.getImageIndex();

            Document document = Jsoup.connect(url).get();
            Elements images = document.select(crawlConfig.getCssSelector());
            if (images.size() <= index) {
                throw new IllegalArgumentException("index is out of range");
            }
            Element image = images.get(index);
            String imageSrc = getImageSrc(url, image);

            log.info("imageSrc: {}", imageSrc);

            try (BufferedInputStream bufferedInputStream = Jsoup.connect(imageSrc)
                    .ignoreContentType(true)
                    .execute().bodyStream()) {
                Path tempFile = Files.createTempFile("temp", ".png");
                Files.copy(bufferedInputStream, tempFile, REPLACE_EXISTING);
                return tempFile;
            }
        } catch (IOException e) {
            throw new ImageCrawlException(e);
        }
    }

    private static String getImageSrc(String url, Element image) {
        String imageSrc = image.attr("src");
        if (!imageSrc.startsWith("http")) {
            imageSrc = url + imageSrc;
        }
        return imageSrc;
    }

}
