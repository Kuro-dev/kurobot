package org.kurodev.discord.command.guild;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author kuro
 **/
public abstract class GuildCommand implements Command {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final String command;
    protected final Permission[] neededPermissions;

    public GuildCommand(String command, Permission... neededPermissions) {
        this.command = command;
        this.neededPermissions = neededPermissions;
    }

    public String getCommand() {
        return command;
    }

    public boolean check(String command, @NotNull GuildMessageReceivedEvent event) {
        return this.command.equalsIgnoreCase(command);
    }

    public abstract void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) throws IOException;

    protected boolean botHasPermission(@NotNull GuildMessageReceivedEvent event) {
        return event.getGuild().getSelfMember().hasPermission(neededPermissions);
    }
}
