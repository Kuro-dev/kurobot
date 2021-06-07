package org.kurodev.discord.message.command.generic.admin;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.DiscordBot;
import org.kurodev.discord.message.command.generic.console.ConsoleCommandHandler;

import java.io.IOException;

public class RestartComputerCommand extends AdminCommand {
    private final ConsoleCommandHandler console = new ConsoleCommandHandler();

    public RestartComputerCommand() {
        super("restart");
    }

    @Override
    protected void prepare(Options args) throws Exception {
        console.prepare();
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        channel.sendMessage("Restarting...").queue();
        console.handle("shutdown /r", channel);
        DiscordBot.getJda().shutdownNow();
    }

    @Override
    public String getDescription() {
        return "restarts the server";
    }
}
