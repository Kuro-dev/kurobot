package org.kurodev.discord.message.command.generic.admin;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kurodev.discord.message.command.enums.CommandType;
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
        return super.check(command, event) && invokerIsAdmin(event);
    }

    @Override
    public boolean needsAdmin() {
        return true;
    }
}
