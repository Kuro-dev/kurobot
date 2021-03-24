package org.kurodev.discord.command.quest;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kuro
 **/
public class QuestHandler {
    private final Map<PlayerData, Quest> quests = new HashMap<>();

    public Map<PlayerData, Quest> getQuests() {
        return quests;
    }

    public void register(GuildMessageReceivedEvent event, Quest q) {
        quests.put(new PlayerData(event), q);
    }

    public boolean exists(PlayerData data) {
        return get(data) != null;
    }

    public boolean exists(GuildMessageReceivedEvent event) {
        return exists(new PlayerData(event));
    }

    public boolean existsForChannel(PlayerData data) {
        return getByGuildChannel(data).size() > 0;
    }

    public List<Quest> getByGuildChannel(PlayerData data) {
        return quests.entrySet().stream().filter(entry -> entry.getKey().guildMatches(data) && entry.getKey().channelMatches(data))
                .map(Map.Entry::getValue).collect(Collectors.toList());
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
