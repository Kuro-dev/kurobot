package org.kurodev.command.commands.impl;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.commands.AdminCommand;

import java.io.IOException;

/**
 * @author kuro
 **/
public class ExitCommand extends AdminCommand {
    public ExitCommand() {
        super("exit");
    }


    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        channel.sendMessage("Bye bye").queue();
        System.exit(0);
    }
}
