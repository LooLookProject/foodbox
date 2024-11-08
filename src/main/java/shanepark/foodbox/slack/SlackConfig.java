package shanepark.foodbox.slack;

import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "slack")
@Getter
@ToString
public class SlackConfig {
    private final String slackUrl;
    private final String slackToken;
    private final String slackChannel;
    private final String userName;

    public SlackConfig(String slackChannel, String slackUrl, String slackToken, String userName) {
        if (!slackChannel.startsWith("#")) {
            slackChannel = "#" + slackChannel;
        }
        if (!slackUrl.endsWith("/")) {
            slackUrl += "/";
        }
        this.slackChannel = slackChannel;
        this.slackUrl = slackUrl;
        this.slackToken = slackToken;
        this.userName = userName;
    }
}
