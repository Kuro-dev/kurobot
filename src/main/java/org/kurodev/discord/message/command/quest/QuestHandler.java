package org.kurodev.discord.message.command.quest;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kuro
 **/
public class QuestHandler {
    private final Map<UserData, Quest> quests = new HashMap<>();

    public Map<UserData, Quest> getQuests() {
        return quests;
    }

    public void register(MessageReceivedEvent event, Quest q) {
        quests.put(UserData.of(event), q);
    }

    public void purgeExpiredQuests() {
        quests.forEach((data, quest) -> {
            if (quest.isExpired() || quest.isFinished()) {
                quests.remove(data);
            }
        });
    }

    public boolean exists(MessageReceivedEvent data) {
        return get(data) != null;
    }

    public Quest get(UserData data) {
        Quest out = quests.get(data);
        if (out != null) {
            if (out.isExpired()) {
                quests.remove(data);
            } else {
                return out;
            }
        }
        return null;
    }

    public Quest get(MessageReceivedEvent event) {
        return get(UserData.of(event));
    }
}
