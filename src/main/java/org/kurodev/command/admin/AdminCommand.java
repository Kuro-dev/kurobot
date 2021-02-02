package org.kurodev.command.admin;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.Command;

/**
 * @author kuro
 **/
public abstract class AdminCommand extends Command {
    public AdminCommand(String command) {
        super(command);
    }

    @Override
    public boolean check(String command, @NotNull GuildMessageReceivedEvent event) {
        boolean preCheck = super.check(command, event);
        return invokerIsAdmin(event) && preCheck;
    }
}
