package org.kurodev.discord.command.guild.standard;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.Command;
import org.kurodev.discord.command.argument.Argument;
import org.kurodev.discord.command.guild.GuildCommand;
import org.kurodev.discord.util.HelpTextFormatter;

import java.util.List;

/**
 * @author kuro
 **/
public class HelpCommand extends GuildCommand {
    private static final String SHOW_ALL = "--all";
    private static final String SHOW_ADMIN_COMMANDS = "--admin";
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
    public void execute(TextChannel channel, Argument args, @NotNull GuildMessageReceivedEvent event) {
        channel.sendTyping().complete();
        boolean isAdminInvoked = false;
        boolean showUnlisted = args.getOpt(SHOW_ALL);
        if (args.getOpt(SHOW_ADMIN_COMMANDS)) {
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
