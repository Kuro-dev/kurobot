package org.kurodev.discord.util;

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
    private final List<String> insults;

    public TextSampleHandler(Path file) {
        this.file = file;
        insults = new ArrayList<>();
    }

    public void prepare() throws IOException {
        if (Files.exists(file))
            insults.addAll(Files.readAllLines(file));
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
        if (insults.size() == 0)
            return "No lines found :( they have either not been added yet or the bot is currently being worked on";
        return insults.get(new Random().nextInt(insults.size()));
    }

    public List<String> getInsults() {
        return insults;
    }
}
