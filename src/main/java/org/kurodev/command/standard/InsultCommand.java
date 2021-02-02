package org.kurodev.command.standard;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.Command;
import org.kurodev.events.InsultHandler;

import java.io.IOException;

/**
 * @author kuro
 **/
public class InsultCommand extends Command {
    private final InsultHandler insults;

    public InsultCommand(InsultHandler insults) {
        super("Insult");
        this.insults = insults;
    }

    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        if (containsMention(event)) {
            //TODO target every mention given
            insults.execute(event, event.getMessage().getMentions());
        } else {
            insults.execute(event, event.getAuthor());
        }
        event.getMessage().delete().queue();
    }

    private boolean containsMention(GuildMessageReceivedEvent event) {
        return !event.getMessage().getMentions(Message.MentionType.USER).isEmpty();
    }
}