package org.kurodev.discord.message.command.guild.standard;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.message.command.generic.GenericCommand;

import java.io.IOException;

/**
 * @author kuro
 **/
public class VersionCommand extends GenericCommand {

    public VersionCommand() {
        super("version");
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        channel.sendMessage(Main.TITLE + " version: " + Main.VERSION).queue();
    }

    @Override
    public String getDescription() {
        return "displays version data";
    }
}
