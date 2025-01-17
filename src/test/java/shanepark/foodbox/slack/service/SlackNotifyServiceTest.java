package shanepark.foodbox.slack.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shanepark.foodbox.api.domain.MenuResponse;
import shanepark.foodbox.api.service.MenuService;
import shanepark.foodbox.slack.SlackConfig;
import shanepark.foodbox.slack.domain.SlackPayload;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlackNotifyServiceTest {


    @InjectMocks
    SlackNotifyService slackNotifyService;

    @Mock
    private SlackMessageSender slackMessageSender;

    @Mock
    SlackConfig slackConfig;

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

    @Test
    @DisplayName("invalid menu(with no line) should not send message")
    void shouldNotSendMessageWhenMenuIsInvalid() throws IOException, InterruptedException {
        MenuResponse invalidMenu1 = new MenuResponse(LocalDate.now(), List.of(""));
        when(menuService.getTodayMenu()).thenReturn(invalidMenu1);

        // when
        slackNotifyService.notifyTodayMenu();

        // then
        verify(slackMessageSender, never()).sendMessage(anyString(), anyString(), any(SlackPayload.class));
    }

    @Test
    @DisplayName("invalid menu(with 1 line) should not send message")
    void shouldNotSendMessageWhenMenuIsInvalid2() throws IOException, InterruptedException {
        MenuResponse invalidMenu1 = new MenuResponse(LocalDate.now(), List.of("oneMenu"));
        when(menuService.getTodayMenu()).thenReturn(invalidMenu1);

        // when
        slackNotifyService.notifyTodayMenu();

        // then
        verify(slackMessageSender, never()).sendMessage(anyString(), anyString(), any(SlackPayload.class));
    }


    @Test
    @DisplayName("invalid menu(with 2 lines) should not send message")
    void shouldNotSendMessageWhenMenuIsInvalid3() throws IOException, InterruptedException {
        MenuResponse invalidMenu1 = new MenuResponse(LocalDate.now(), List.of("oneMenu", "twoMenu"));
        when(menuService.getTodayMenu()).thenReturn(invalidMenu1);

        // when
        slackNotifyService.notifyTodayMenu();

        // then
        verify(slackMessageSender, never()).sendMessage(anyString(), anyString(), any(SlackPayload.class));
    }

    @Test
    @DisplayName("valid menu(with 3 lines) should send message")
    void shouldNotSendMessageWhenMenuIsInvalid4() throws IOException, InterruptedException {
        MenuResponse invalidMenu1 = new MenuResponse(LocalDate.now(), List.of("oneMenu", "twoMenu", "threeMenu"));
        when(menuService.getTodayMenu()).thenReturn(invalidMenu1);
        mockSlackConfig();

        // when
        slackNotifyService.notifyTodayMenu();

        // then send message once
        verify(slackMessageSender, times(1)).sendMessage(anyString(), anyString(), any(SlackPayload.class));
    }

    private void mockSlackConfig() {
        when(slackConfig.getSlackUrl()).thenReturn("https://hooks");
        when(slackConfig.getSlackChannel()).thenReturn("foodbox");
        when(slackConfig.getUserName()).thenReturn("점심봇");
        when(slackConfig.getSlackToken()).thenReturn("");
    }


}
