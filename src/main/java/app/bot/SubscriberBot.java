package app.bot;

import app.bot.extensions.SubscribeExtension;
import app.views.UserListView;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collection;


@Component
public class SubscriberBot extends AbilityBot {
    private final SubscriberBotConfig config;

    private final long creatorId;

    public SubscriberBot(SubscriberBotConfig config) {
        super(config.getToken(), config.getUsername());

        creatorId = config.getCreatorId();

        this.config = config;
    }

    @Override
    public long creatorId() {
        return creatorId;
    }

    public AbilityExtension subscribeExtension() {
        return new SubscribeExtension(this,  config.getSubscriptionService());
    }

    private void getBotUsers(MessageContext context) {
        final Collection<User> users = users().values();

        silent().execute(UserListView.buildMessage(context.chatId(), users));
    }

    public Ability getBotUsers() {
        return Ability.builder()
                .name("users")
                .locality(Locality.ALL)
                .privacy(Privacy.ADMIN)
                .action(this::getBotUsers)
                .build();
    }
}
