package shanepark.foodbox.slack.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shanepark.foodbox.slack.service.SlackNotifyService;

import java.io.IOException;

@RestController
@RequestMapping("/api/slack")
@RequiredArgsConstructor
public class SlackNotifyController {

    private final SlackNotifyService slackNotifyService;

    @GetMapping("/notify")
    public ResponseEntity<Boolean> notifyTodayMenu() throws IOException, InterruptedException {
        slackNotifyService.notifyTodayMenu();
        return ResponseEntity.ok(true);
    }

}
