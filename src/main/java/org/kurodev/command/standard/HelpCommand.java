package org.kurodev.command.standard;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.Command;
import org.kurodev.command.admin.AdminCommand;

import java.util.List;

/**
 * @author kuro
 **/
@SuppressWarnings("ResultOfMethodCallIgnored")
public class HelpCommand extends Command {
    private final List<Command> commands;

    public HelpCommand(List<Command> commands) {
        super("Help");
        this.commands = commands;
    }

    //TODO add formatting using ``` text ``` formatting and make it display a table
    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) {
        channel.sendTyping().complete();
        MessageAction msg = channel.sendMessage("you should really seek counselling\n");
        msg.append("But here are all the known commands:\n");
        commands.stream().filter(command -> !(command instanceof AdminCommand)).forEach(command ->
                msg.append(command.getCommand()).append("\n"));

        if (invokerIsAdmin(event)) {
            msg.append("-------------------\n").append("Admin commands:\n");
            commands.stream().filter(command -> (command instanceof AdminCommand)).forEach(command ->
                    msg.append(command.getCommand()).append("\n"));
        }
        msg.append("-------------------\n");
        msg.append("Please remember to use \"!k *command*\" to execute the command.").queue();
    }
}
