package org.kurodev.discord.message.command.generic.admin;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kurodev.discord.message.command.CommandType;
import org.kurodev.discord.message.command.generic.GenericCommand;

/**
 * @author kuro
 **/
public abstract class AdminCommand extends GenericCommand {
    public AdminCommand(String command) {
        super(command, CommandType.ADMIN);
    }

    @Override
    public boolean check(String command, MessageReceivedEvent event) {
        boolean preCheck = super.check(command, event);
        return preCheck && invokerIsAdmin(event);
    }

    @Override
    public boolean needsAdmin() {
        return true;
    }
}
