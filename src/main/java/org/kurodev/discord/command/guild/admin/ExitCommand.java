package org.kurodev.discord.command.guild.admin;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.command.argument.Argument;

import java.io.IOException;

/**
 * @author kuro
 **/
public class ExitCommand extends AdminCommand {
    public ExitCommand() {
        super("exit");
    }


    @Override
    public void execute(TextChannel channel, Argument args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        channel.sendMessage("Shutting down bot").queue();
        Main.getJDA().shutdown();
    }

    @Override
    public String getDescription() {
        return "shuts down the bot";
    }
}