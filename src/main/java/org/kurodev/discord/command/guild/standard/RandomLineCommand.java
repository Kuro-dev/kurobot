package org.kurodev.discord.command.guild.standard;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.argument.Argument;
import org.kurodev.discord.command.guild.GuildCommand;
import org.kurodev.discord.events.TextSampleHandler;

import java.io.IOException;

/**
 * @author kuro
 **/
public class RandomLineCommand extends GuildCommand {
    private final TextSampleHandler insults;


    /**
     * @param command The command where this instance is triggered from
     * @param samples A collection of one line samples from which will be one line randomly picked
     */
    public RandomLineCommand(String command, TextSampleHandler samples) {
        super(command);
        this.insults = samples;
    }

    @Override
    public boolean supportsMention() {
        return true;
    }

    @Override
    public void prepare() throws Exception {
        insults.prepare();
    }

    @Override
    public void execute(TextChannel channel, Argument args, @NotNull GuildMessageReceivedEvent event) throws IOException {
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

    @Override
    public String getDescription() {
        return "used to insult the invoker or the person @mentioned after the command";
    }
}
