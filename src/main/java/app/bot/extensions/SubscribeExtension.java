package app.bot.extensions;

import app.services.SubscriptionService;
import app.views.UserListView;
import com.google.common.base.Strings;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubscribeExtension implements AbilityExtension {

    private final AbilityBot bot;

    private final SubscriptionService subscriptionService;


    public SubscribeExtension(AbilityBot bot, SubscriptionService subscriptionService) {
        this.bot = bot;
        this.subscriptionService = subscriptionService;
    }

    private boolean subscribeByUserName(String s) {
        return Optional.ofNullable(s)
                .filter(o -> !Strings.isNullOrEmpty(o))
                .map(AbilityUtils::stripTag)
                .map(bot.userIds()::get)
                .map(subscriptionService::subscribe)
                .orElse(false);
    }

    private boolean subscribeByChatId(long chatId) {
        return Optional.of(chatId)
                .filter(bot.users()::containsKey)
                .map(subscriptionService::subscribe)
                .orElse(false);
    }

    private void subscribe(MessageContext context) {
        final String input = context.firstArg();

        boolean result;

        try {
            result = subscribeByChatId(Long.parseLong(input));
        } catch (NumberFormatException e) {
            result = subscribeByUserName(input);
        }

        if (!result) {
            bot.silent().send("User not found / already subscribed", context.chatId());
        } else {
            bot.silent().send("User successfully subscribed", context.chatId());
        }
    }

    public Ability subscribe() {
        return Ability.builder()
                .name("subscribe")
                .info("[username/userId] - subscribe user to notifications")
                .locality(Locality.ALL)
                .privacy(Privacy.ADMIN)
                .input(1)
                .action(this::subscribe)
                .build();
    }

    private void subscribers(MessageContext context) {
        final List<User> users = subscriptionService.getSubscribers().stream()
                .map(bot.users()::get)
                .collect(Collectors.toList());

        bot.silent().execute(UserListView.buildMessage(context.chatId(), users));
    }

    public Ability subscribers() {
        return Ability.builder()
                .name("subscribers")
                .info("returns list of subscribers")
                .locality(Locality.ALL)
                .privacy(Privacy.ADMIN)
                .action(this::subscribers)
                .build();
    }

    private void unsubscribe(MessageContext context) {
        final String input = context.firstArg();

        boolean result;

        try {
            result = unsubscribeByChatId(Long.parseLong(input));
        } catch (NumberFormatException e) {
            result = unsubscribeByUserName(input);
        }

        if (!result) {
            bot.silent().send("Subscriber not found", context.chatId());
        } else {
            bot.silent().send("User successfully unsubscribed", context.chatId());
        }
    }

    private boolean unsubscribeByUserName(String s) {
        return Optional.ofNullable(s)
                .filter(o -> !Strings.isNullOrEmpty(o))
                .map(AbilityUtils::stripTag)
                .map(bot.userIds()::get)
                .map(subscriptionService::unsubscribe)
                .orElse(false);
    }

    private boolean unsubscribeByChatId(long chatId) {
        return Optional.of(chatId)
                .filter(bot.users()::containsKey)
                .map(subscriptionService::unsubscribe)
                .orElse(false);
    }

    public Ability unsubscribe() {
        return Ability.builder()
                .name("unsubscribe")
                .info("[username/userId] - unsubscribe user to notifications")
                .locality(Locality.ALL)
                .privacy(Privacy.ADMIN)
                .input(1)
                .action(this::unsubscribe)
                .build();
    }
}
