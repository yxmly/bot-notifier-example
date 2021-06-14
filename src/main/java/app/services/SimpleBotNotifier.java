package app.services;

import app.bot.SubscriberBot;
import org.springframework.stereotype.Component;

@Component
public class SimpleBotNotifier implements Notifier {
    private final SubscriptionService service;
    private final SubscriberBot bot;

    public SimpleBotNotifier(
            SubscriptionService service,
            SubscriberBot bot
    ) {
        this.service = service;
        this.bot = bot;
    }

    @Override
    public void notifyAll(String message) {
        service.getSubscribers().forEach(subscriber -> {
            bot.silent().send(message, subscriber);
        });
    }
}
