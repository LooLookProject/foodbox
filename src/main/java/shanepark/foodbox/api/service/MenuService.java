package shanepark.foodbox.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shanepark.foodbox.api.domain.MenuResponse;
import shanepark.foodbox.api.exception.MenuNotUploadedException;
import shanepark.foodbox.api.repository.MenuRepository;
import shanepark.foodbox.crawl.CrawlConfig;
import shanepark.foodbox.crawl.MenuCrawler;
import shanepark.foodbox.image.ImageParser;
import shanepark.foodbox.image.domain.ParsedMenu;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuCrawler menuCrawler;
    private final ImageParser imageParser;
    private final CrawlConfig crawlConfig;

    public MenuResponse getTodayMenu() {
        LocalDate today = LocalDate.now();
        return menuRepository.findByDate(today)
                .orElseGet(() -> {
                    crawl();
                    return menuRepository.findByDate(today).orElseThrow(MenuNotUploadedException::new);
                });
    }

    public List<MenuResponse> findAll() {
        // ensure today's menu is always up-to-date
        getTodayMenu();
        return menuRepository.findAll();
    }

    private void crawl() {
        InputStream inputStream = menuCrawler.getImage(crawlConfig);
        List<ParsedMenu> parsed = imageParser.parse(inputStream);
        for (ParsedMenu menu : parsed) {
            MenuResponse resp = menu.toMenuResponse();
            menuRepository.save(resp);
        }
    }

}
