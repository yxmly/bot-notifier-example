package app.bot;

import app.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("/bot.properties")
public class SubscriberBotConfig {
    private final String username;
    private final String token;
    private final Long creatorId;

    private final SubscriptionService subscriptionService;

    public SubscriberBotConfig(
            @Value("${bot.username}") String username,
            @Value("${bot.token}") String token,
            @Value("${bot.creatorId}") Long creatorId,
            SubscriptionService subscriptionService) {
        this.username = username;
        this.token = token;
        this.creatorId = creatorId;
        this.subscriptionService = subscriptionService;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public SubscriptionService getSubscriptionService() {
        return subscriptionService;
    }
}
