package org.kurodev.discord.message.command.generic.admin;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.DiscordBot;
import org.kurodev.discord.message.command.generic.console.ConsoleCommandHandler;

import java.io.IOException;

public class RestartComputerCommand extends AdminCommand {
    private final ConsoleCommandHandler console = new ConsoleCommandHandler();
    private final Runnable additionalShutdownContext;

    public RestartComputerCommand(Runnable additionalShutdownContext) {
        super("restart");
        this.additionalShutdownContext = additionalShutdownContext;
    }

    @Override
    protected void prepare(Options args) throws Exception {
        console.prepare();
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        channel.sendMessage("Restarting...").queue();
        console.handle("shutdown /r", channel);
        informAdmins(event.getAuthor());
        DiscordBot.getJda().shutdown();
        if (additionalShutdownContext != null) {
            additionalShutdownContext.run();
        }
    }

    private void informAdmins(User user) {
        final String msg = String.format("%s has initiated a system restart", user.getName());
        for (Long aLong : Main.ADMINS.getAll()) {
            User admin = DiscordBot.getJda().retrieveUserById(aLong).complete();
            PrivateChannel channel = admin.openPrivateChannel().complete();
            channel.sendMessage(msg).queue();
        }
    }

    @Override
    public String getDescription() {
        return "restarts the server";
    }
}
