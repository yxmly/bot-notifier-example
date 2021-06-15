package app.views;

import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserListView {
    private static String from(Collection<User> users) {
        final List<String> userList = users.stream().map(u -> {
            final StringBuilder sb = new StringBuilder();

            sb
                    .append("<i>")
                    .append(AbilityUtils.fullName(u))
                    .append("</i>")
                    .append(" | ");

            if (u.getUserName() == null) {
                sb.append("<b>").append(u.getId()).append("</b>");
            } else {
                sb.append(AbilityUtils.addTag(u.getUserName()));
            }

            return sb.toString();
        }).collect(Collectors.toList());

        return "<strong>" + users.size() + "</strong> user(s):\n"
                + String.join("\n", userList);
    }

    public static SendMessage buildMessage(Long chatId, Collection<User> users) {
        Objects.requireNonNull(chatId);
        Objects.requireNonNull(users);

        return SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text(from(users))
                .parseMode(ParseMode.HTML)
                .build();
    }
}
