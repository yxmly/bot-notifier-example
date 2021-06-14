package app.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SubscriptionService {
    private final Set<Long> chats = new HashSet<>();

    public boolean subscribe(Long chatId) {
        return chats.add(chatId);
    }

    public boolean unsubscribe(Long chatId) {
        return chats.remove(chatId);
    }

    public List<Long> getSubscribers() {
        return new ArrayList<>(chats);
    }
}
