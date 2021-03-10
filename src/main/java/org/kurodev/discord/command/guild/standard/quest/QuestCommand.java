package org.kurodev.discord.command.guild.standard.quest;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.argument.Argument;
import org.kurodev.discord.command.guild.GuildCommand;

import java.io.IOException;

/**
 * @author kuro
 **/
public abstract class QuestCommand extends GuildCommand {

    public QuestCommand(String command, Permission... neededPermissions) {
        super(command, neededPermissions);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void execute(TextChannel channel, Argument args, @NotNull GuildMessageReceivedEvent event) throws IOException {

    }
}
