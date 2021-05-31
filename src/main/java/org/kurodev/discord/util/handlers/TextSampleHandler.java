package org.kurodev.discord.util.handlers;

import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

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


    public void execute(@NotNull MessageReceivedEvent event, List<IMentionable> mentions) {
        MessageChannel channel = event.getTextChannel();
        if (mentions != null && !mentions.isEmpty()) {
            for (IMentionable mention : mentions) {
                channel.sendMessage(mention.getAsMention() + " " + getRandomInsult().trim()).mention(mention).queue();
            }
        } else {
            channel.sendMessage(getRandomInsult()).queue();
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
