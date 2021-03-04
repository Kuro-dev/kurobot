package org.kurodev.command.guild.standard;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.Command;
import org.kurodev.command.guild.GuildCommand;
import org.kurodev.util.HelpTextFormatter;

import java.util.List;

/**
 * @author kuro
 **/
public class HelpCommand extends GuildCommand {
    private final List<? extends Command> commands;

    public HelpCommand(List<? extends Command> commands) {
        super("Help");
        this.commands = commands;
    }

    @Override
    public String getDescription() {
        return "Lists all available commands. Possible arguments: -admin";
    }

    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) {
        channel.sendTyping().complete();
        boolean isAdminInvoked = false;
        boolean showUnlisted = argsContain("-all", args);
        if (argsContain("-admin", args)) {
            if (invokerIsAdmin(event)) {
                isAdminInvoked = true;
            } else {
                channel.sendMessage("nice try, but you're not an admin ;)")
                        .append("\nhere are the commands YOU can use:\n").queue();
            }
        }
        channel.sendMessage(HelpTextFormatter.format(commands, isAdminInvoked, showUnlisted)).queue();
    }
}
