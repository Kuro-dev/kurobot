package org.kurodev.discord.message.command.guild.standard;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.message.command.guild.GuildCommand;
import org.kurodev.discord.util.handlers.TextSampleHandler;

import java.io.IOException;

/**
 * @author kuro
 **/
public class RandomLineCommand extends GuildCommand {
    private final TextSampleHandler samples;


    public RandomLineCommand(String command, TextSampleHandler samples) {
        super(command);
        this.samples = samples;
    }

    @Override
    public boolean supportsMention() {
        return true;
    }

    @Override
    public void prepare(Options args) throws Exception {
        logger.info("Preparing samples");
        samples.prepare();
        logger.info("Preparing samples - DONE");
    }

    @Override
    public void execute(TextChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        //TODO target every mention given
        samples.execute(event, event.getMessage().getMentions());
    }

    private boolean containsMention(@NotNull MessageReceivedEvent event) {
        return !event.getMessage().getMentions(Message.MentionType.USER).isEmpty();
    }

    @Override
    public String getDescription() {
        return "used to insult the invoker or the person @mentioned after the command";
    }
}
