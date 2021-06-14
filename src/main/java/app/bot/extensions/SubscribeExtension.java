package app.bot.extensions;

import app.services.SubscriptionService;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.util.AbilityExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

public class SubscribeExtension implements AbilityExtension {

    private final AbilityBot bot;

    private final SubscriptionService subscriptionService;


    public SubscribeExtension(AbilityBot bot, SubscriptionService subscriptionService) {
        this.bot = bot;
        this.subscriptionService = subscriptionService;
    }

    private void subscribe(MessageContext context) {
        if (subscriptionService.subscribe(context.chatId())) {
            bot.silent().send("Successfully subscribed", context.chatId());
        } else {
            bot.silent().send("This chat is already subscribed", context.chatId());
        }
    }

    private void unsubscribe(MessageContext context) {
        if (subscriptionService.unsubscribe(context.chatId())) {
            bot.silent().send("Successfully unsubscribed", context.chatId());
        } else {
            bot.silent().send("This chat is not subscribed to notifications", context.chatId());
        }
    }

    private void getSubscribers(MessageContext context) {
        final List<String> chatIds = subscriptionService.getSubscribers().stream()
                .map(Object::toString).collect(Collectors.toList());

        final String subscribers = String.join(",\n", chatIds);

        bot.silent().send("Subscribers: \n" + subscribers, context.chatId());
    }

    public Ability subscribe() {
        return Ability.builder()
                .name("subscribe")
                .info("Subscribe to notification")
                .locality(Locality.ALL)
                .privacy(PUBLIC)
                .action(this::subscribe)
                .build();
    }

    public Ability unsubscribe() {
        return Ability.builder()
                .name("unsubscribe")
                .info("Unsubscribe from further notifications")
                .locality(Locality.ALL)
                .privacy(PUBLIC)
                .action(this::unsubscribe)
                .build();
    }

    public Ability subscribers() {
        return Ability.builder()
                .name("subscribers")
                .info("Get list of subscribers")
                .locality(Locality.ALL)
                .privacy(PUBLIC)
                .action(this::getSubscribers)
                .build();
    }

}
