package org.kurodev.discord.message.command.generic.admin;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.DiscordBot;

import java.io.IOException;

/**
 * @author kuro
 **/
public class ExitCommand extends AdminCommand {
    private final Runnable additionalShutDownContext;

    public ExitCommand(Runnable additionalShutDownContext) {
        super("exit");
        this.additionalShutDownContext = additionalShutDownContext;
    }

    @Override
    public String getDescription() {
        return "shuts down the bot";
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        channel.sendMessage("Shutting down bot").queue();
        for (Object registeredListener : DiscordBot.getJda().getEventManager().getRegisteredListeners()) {
            DiscordBot.getJda().removeEventListener(registeredListener);
        }
        DiscordBot.getJda().shutdown();
        if (additionalShutDownContext != null)
            additionalShutDownContext.run();
    }
}
