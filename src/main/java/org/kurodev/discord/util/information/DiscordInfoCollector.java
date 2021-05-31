package org.kurodev.discord.util.information;

import org.kurodev.discord.DiscordBot;
import org.kurodev.discord.command.guild.GuildCommand;

import java.util.List;

public class DiscordInfoCollector {
    private static DiscordInfoCollector instance;
    private final DiscordBot bot;

    private DiscordInfoCollector(DiscordBot bot) {

        this.bot = bot;
    }

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
        List<GuildCommand> commands = bot.getMessageEventHandler().getGuildCommandHandler().getCommands();
        return new DiscordInfo(commands);
    }
}
