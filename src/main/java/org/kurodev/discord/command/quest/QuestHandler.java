package org.kurodev.discord.command.quest;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kuro
 **/
public class QuestHandler {
    private final Map<PlayerData, Quest> quests = new HashMap<>();

    public void register(GuildMessageReceivedEvent event, Quest q) {
        quests.put(new PlayerData(event), q);
    }

    public boolean exists(PlayerData data) {
        return get(data) != null;
    }

    public boolean exists(GuildMessageReceivedEvent event) {
        return exists(new PlayerData(event));
    }

    public Quest get(PlayerData data) {
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

    public Quest get(GuildMessageReceivedEvent event) {
        return get(new PlayerData(event));
    }
}
