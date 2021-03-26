package org.kurodev.discord.command.privateMsg;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.interfaces.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author kuro
 **/
public abstract class PrivateCommand implements Command {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final String command;

    public PrivateCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public boolean check(String command) {
        return this.command.equalsIgnoreCase(command);
    }

    public abstract void execute(PrivateChannel channel, String[] args, @NotNull PrivateMessageReceivedEvent event) throws IOException;

}
