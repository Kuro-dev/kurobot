package org.kurodev.discord.message.command.guild;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.message.command.enums.CommandType;
import org.kurodev.discord.message.command.generic.GenericCommand;

import java.io.IOException;

/**
 * @author kuro
 **/
public abstract class GuildCommand extends GenericCommand {

    public GuildCommand(String command, Permission... neededPermissions) {
        super(command, CommandType.GUILD, neededPermissions);
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        execute((TextChannel) channel, args, event);
    }

    @Override
    public boolean check(String command, MessageReceivedEvent event) {
        return super.check(command, event) && event.isFromGuild();
    }

    protected abstract void execute(TextChannel channel, CommandLine args, MessageReceivedEvent event) throws IOException;

}
