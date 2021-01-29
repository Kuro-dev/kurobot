package org.kurodev.command.commands;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.UserIDs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author kuro
 **/
public abstract class Command {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final String command;

    public Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public boolean check(String command, @NotNull GuildMessageReceivedEvent event) {
        return this.command.equalsIgnoreCase(command);
    }

    protected boolean invokerIsAdmin(GuildMessageReceivedEvent event) {
        return invokerIsAdmin(event.getAuthor());
    }

    protected boolean invokerIsAdmin(User user) {
        return user.getId().equals(UserIDs.kuro.getId());
    }

    public abstract void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) throws IOException;
}
