package org.kurodev.discord.util.handlers;

import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author kuro
 **/
public class TextSampleHandler {
    private final Path file;

    private final List<String> samples;

    public TextSampleHandler(Path file) {
        this.file = file;
        samples = new ArrayList<>();
    }

    public void prepare() throws IOException {
        if (Files.exists(file))
            samples.addAll(Files.readAllLines(file));
    }

    public void execute(GuildMessageReceivedEvent event) {
        Member member = event.getGuild().getMember(event.getAuthor());
        execute(event, member);
    }

    public void execute(GuildMessageReceivedEvent event, IMentionable mention) {
        TextChannel channel = event.getChannel();
        String insult = getRandomInsult();
        if (mention != null) {
            channel.sendMessage(mention.getAsMention() + " " + insult.trim()).mention(mention).queue();
        } else {
            channel.sendMessage(insult).queue();
        }
    }

    public void execute(GuildMessageReceivedEvent event, List<IMentionable> mentions) {
        for (IMentionable mention : mentions) {
            execute(event, mention);
        }
    }

    private String getRandomInsult() {
        if (samples.size() == 0)
            return "No lines found :( they have either not been added yet or the bot is currently being worked on";
        return samples.get(new Random().nextInt(samples.size()));
    }

    public List<String> getSamples() {
        return samples;
    }
}
