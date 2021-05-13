package org.kurodev.discord.command.guild.admin;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
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
    public void execute(TextChannel channel, CommandLine args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        channel.sendMessage("Shutting down bot").queue();
        for (Object registeredListener : DiscordBot.getJDA().getEventManager().getRegisteredListeners()) {
            DiscordBot.getJDA().removeEventListener(registeredListener);
        }
        DiscordBot.getJDA().shutdown();
        if (additionalShutDownContext != null)
            additionalShutDownContext.run();
    }

    @Override
    public String getDescription() {
        return "shuts down the bot";
    }
}
