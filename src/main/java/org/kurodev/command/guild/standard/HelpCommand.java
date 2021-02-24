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
@SuppressWarnings("ResultOfMethodCallIgnored")
public class HelpCommand extends GuildCommand {
    private final List<? extends Command> commands;
    private String response;

    public HelpCommand(List<? extends Command> commands) {
        super("Help");
        this.commands = commands;
    }

    @Override
    public void prepare() throws Exception {
        response = HelpTextFormatter.format(commands);
    }
    @Override
    public String getDescription() {
        return "lists all available commands";
    }
    //TODO add formatting using ``` text ``` formatting and make it display a table
    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) {
        channel.sendTyping().complete();
        channel.sendMessage(response).queue();
    }
}
