package shanepark.foodbox.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import shanepark.foodbox.api.exception.ImageCrawlException;

import java.io.IOException;
import java.io.InputStream;

@Component
public class MenuCrawler {

    public InputStream getImage(CrawlConfig crawlConfig) {
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

            return Jsoup.connect(imageSrc)
                    .ignoreContentType(true)
                    .execute().bodyStream();
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
