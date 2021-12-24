package org.kurodev.discord.util.information;

import org.kurodev.Main;
import org.kurodev.config.Setting;
import org.kurodev.discord.DiscordBot;
import org.kurodev.discord.message.State;

import javax.validation.constraints.NotNull;
import java.util.List;

public class DiscordInfo {
    @NotNull
    private final String name;
    @NotNull
    private final State state;
    @NotNull
    private final String version;
    @NotNull
    private final List<CommandInformation> commands;

    public DiscordInfo(DiscordBot bot) {
        var name = Main.SETTINGS.getSetting(Setting.BOT_NAME);
        this.name = name != null ? name : "no name found";
        this.version = Main.getVersion();
        this.state = bot.getMessageEventHandler().getState();
        this.commands = CommandInformation.of(bot.getMessageEventHandler().getCommandHandler().getCommands());
    }

    public List<CommandInformation> getCommandInfo() {
        return commands;
    }

    public State getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
