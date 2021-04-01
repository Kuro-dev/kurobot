package org.kurodev.discord.command.guild.standard;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
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
    protected void prepare(Options args) throws Exception {

    }

    @Override
    public void execute(TextChannel channel, CommandLine args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        channel.sendMessage(Main.TITLE + " version: " + Main.VERSION).queue();
    }
}
