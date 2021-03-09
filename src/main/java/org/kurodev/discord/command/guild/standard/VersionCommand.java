package org.kurodev.discord.command.guild.standard;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.command.argument.Argument;
import org.kurodev.discord.command.guild.GuildCommand;

import java.io.IOException;

/**
 * @author kuro
 **/
public class VersionCommand extends GuildCommand {

    public VersionCommand() {
        super("version");
    }

    @Override
    public String getDescription() {
        return "displays version data";
    }

    @Override
    public void execute(TextChannel channel, Argument args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        channel.sendMessage(Main.TITLE + " version: " + Main.VERSION).queue();
    }
}
