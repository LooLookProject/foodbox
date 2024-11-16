package shanepark.foodbox.slack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shanepark.foodbox.api.domain.MenuResponse;
import shanepark.foodbox.api.service.MenuService;
import shanepark.foodbox.slack.SlackConfig;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SlackNotifyServiceTest {

    @Mock
    MenuService menuService;

    @Test
    void notifyTodayMenu() throws IOException, InterruptedException {
        // Given
        String slackToken = "SLACK_TOKEN_HERE_FOR_REAL_TEST";
        SlackConfig slackConfig = new SlackConfig("foodbox", "https://hooks.slack.com/services", slackToken, "점심봇");
        SlackMessageSender slackMessageSender = new SlackMessageSender();
        SlackNotifyService slackNotifyService = new SlackNotifyService(menuService, slackMessageSender, slackConfig);

        // When
        LocalDate now = LocalDate.now();
        if (now.getDayOfWeek().getValue() <= 5) {
            when(menuService.getTodayMenu()).thenReturn(new MenuResponse(LocalDate.of(2024, 11, 8), List.of("김치찌개", "된장찌개", "제육볶음")));
        }

        // Then
        slackNotifyService.notifyTodayMenu();
    }

}
