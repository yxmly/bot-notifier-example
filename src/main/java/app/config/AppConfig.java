package app.config;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "app")
public class AppConfig {

    private final ApplicationContext ctx;

    public AppConfig(ApplicationContext appContext) {
        this.ctx = appContext;
    }

    @Bean
    public TelegramBotsApi botsApi(List<LongPollingBot> bots) {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);

            for (LongPollingBot bot : bots) {
                api.registerBot(bot);
            }

            return api;
        } catch (TelegramApiException e) {
            throw new BeanCreationException("botsApi", e);
        }
    }
}
