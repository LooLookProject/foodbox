package shanepark.foodbox.crawl;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "crawl")
public record CrawlConfig(
        String crawlUrl,
        String cssSelector,
        int imageIndex
) {
}
