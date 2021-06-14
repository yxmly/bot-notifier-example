package app.bot;

import app.bot.extensions.SubscribeExtension;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.util.AbilityExtension;


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

    public Ability sayHelloWorld() {
        return Ability.builder()
                .name("hello")
                .info("says hello world")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> silent.send("Hello, world!", ctx.chatId()))
                .build();
    }

    public AbilityExtension subscribeExtension() {
        return new SubscribeExtension(this,  config.getSubscriptionService());
    }
}
