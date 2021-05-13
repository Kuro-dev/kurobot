package org.kurodev.discord.util.information;

import org.kurodev.discord.command.guild.GuildCommand;

import java.util.List;

public class DiscordInfo {
    private final List<CommandInformation> commands;

    public DiscordInfo(List<GuildCommand> commands) {
        this.commands = CommandInformation.of(commands);
    }

    public List<CommandInformation> getCommandInfo() {
        return commands;
    }
}
