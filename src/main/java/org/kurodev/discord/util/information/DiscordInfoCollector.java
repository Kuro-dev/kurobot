package org.kurodev.discord.util.information;

import org.kurodev.discord.DiscordBot;
import org.kurodev.discord.message.State;

public record DiscordInfoCollector(DiscordBot bot) {
    private static DiscordInfoCollector instance;

    public static void createInstance(DiscordBot bot) {
        if (instance != null) {
            throw new IllegalStateException("Instance must only be assigned once");
        }
        instance = new DiscordInfoCollector(bot);
    }

    public static DiscordInfoCollector getInstance() {
        return instance;
    }

    public DiscordInfo collect() {
        return new DiscordInfo(bot);
    }

    public boolean isOnline() {
        return bot.getMessageEventHandler().getState() == State.ONLINE;
    }
}
