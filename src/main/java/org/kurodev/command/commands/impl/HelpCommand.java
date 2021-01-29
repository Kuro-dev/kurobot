package org.kurodev.command.commands.impl;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.commands.AdminCommand;
import org.kurodev.command.commands.Command;

import java.util.List;

/**
 * @author kuro
 **/
public class HelpCommand extends Command {
    private final List<Command> commands;

    public HelpCommand(List<Command> commands) {
        super("help");
        this.commands = commands;
    }

    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) {
        channel.sendTyping().complete();
        MessageAction msg = channel.sendMessage("you should really seek counselling\n");
        msg.append("But here are all the known commands:\n");
        if (invokerIsAdmin(event))
            commands.forEach(command -> {
                msg.append("\t -").append(command.getCommand()).append("\n");
            });
        else
            commands.stream().filter(command -> !(command instanceof AdminCommand)).forEach(command -> {
                msg.append(command.getCommand()).append("\n");
            });
        msg.queue();
    }
}
