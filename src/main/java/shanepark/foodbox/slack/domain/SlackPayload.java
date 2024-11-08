package shanepark.foodbox.slack.domain;

public record SlackPayload(
        String channel,
        String username,
        String text,
        String icon_emoji
) {

}
