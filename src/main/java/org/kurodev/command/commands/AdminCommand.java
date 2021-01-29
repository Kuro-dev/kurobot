package org.kurodev.command.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author kuro
 **/
public abstract class AdminCommand extends Command {
    public AdminCommand(String command) {
        super(command);
    }

    @Override
    public boolean check(String command, @NotNull GuildMessageReceivedEvent event) {
        boolean check1 = super.check(command, event);
        return invokerIsAdmin(event) && check1;
    }
}
