package org.kurodev.discord.message.command.generic.standard;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.message.command.generic.GenericCommand;

import java.io.IOException;

/**
 * @author kuro
 **/
public class ArchCommand extends GenericCommand {
    public ArchCommand() {
        super("getArch");
    }

    @Override
    public String getDescription() {
        return "returns the operating system running on the bot";
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        channel.sendMessage(System.getProperty("os.name")).queue();

    }
}
