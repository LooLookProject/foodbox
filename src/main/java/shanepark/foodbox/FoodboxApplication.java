package shanepark.foodbox;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import shanepark.foodbox.crawl.CrawlConfig;
import shanepark.foodbox.slack.SlackConfig;

@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class FoodboxApplication {

    private final CrawlConfig crawlConfig;
    private final SlackConfig slackConfig;

    @PostConstruct
    public void init() {
        String arch = System.getProperty("os.arch");
        String os = System.getProperty("os.name");
        log.info("os: {}, arch: {}", os, arch);

        if (os.contains("Mac") && arch.contains("aarch64")) {
            System.setProperty("jna.library.path", "/opt/homebrew/opt/tesseract/lib");
        }

        log.info("CrawlConfig: {}", crawlConfig);
        log.info("SlackConfig: {}", slackConfig);
    }

    public static void main(String[] args) {
        SpringApplication.run(FoodboxApplication.class, args);
    }

}
