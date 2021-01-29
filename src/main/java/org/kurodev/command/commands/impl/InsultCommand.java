package org.kurodev.command.commands.impl;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.commands.Command;
import org.kurodev.events.InsultHandler;

import java.io.IOException;

/**
 * @author kuro
 **/
public class InsultCommand extends Command {
    private final InsultHandler insults;

    public InsultCommand(InsultHandler insults) {
        super("insultMe");
        this.insults = insults;
    }

    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        insults.execute(event);
    }
}
