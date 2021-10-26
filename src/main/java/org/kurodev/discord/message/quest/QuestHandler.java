package org.kurodev.discord.message.quest;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

/**
 * @author kuro
 **/
public class QuestHandler {
    private final Map<UserData, Set<Quest>> quests = new HashMap<>();

    public Map<UserData, Set<Quest>> getQuests() {
        return quests;
    }

    public void register(MessageReceivedEvent event, Quest q) {
        quests.computeIfAbsent(UserData.of(event), userData -> new HashSet<>()).add(q);
    }

    public void purgeExpiredQuests() {
        quests.forEach((data, quests) -> {
            quests.removeIf(quest -> quest.isExpired() || quest.isFinished());
        });
    }

    public boolean exists(MessageReceivedEvent data) {
        return get(data) != null;
    }

    public Set<Quest> get(UserData data) {
        var out = quests.get(data);
        if (out != null) {
            purgeExpiredQuests();
            return out;
        }
        return null;
    }

    public Set<Quest> get(MessageReceivedEvent event) {
        return get(UserData.of(event));
    }
}
