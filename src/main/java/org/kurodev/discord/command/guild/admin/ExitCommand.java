package org.kurodev.discord.command.guild.admin;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;

import java.io.IOException;

/**
 * @author kuro
 **/
public class ExitCommand extends AdminCommand {
    public ExitCommand() {
        super("exit");
    }


    @Override
    protected void prepare(Options args) throws Exception {

    }

    @Override
    public void execute(TextChannel channel, CommandLine args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        channel.sendMessage("Shutting down bot").queue();
        Main.getJDA().shutdown();
    }

    @Override
    public String getDescription() {
        return "shuts down the bot";
    }
}
