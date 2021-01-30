package org.kurodev.events;

import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.kurodev.Main;
import org.kurodev.config.Setting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author kuro
 **/
public class InsultHandler {
    private static final Path FILE = Paths.get(Main.SETTINGS.getSetting(Setting.INSULT_FILE));
    private final List<String> insults;

    public InsultHandler() {
        if (Files.exists(FILE)) {
            try {
                insults = Files.readAllLines(FILE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            insults = new ArrayList<>();
        }
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

    private String getRandomInsult() {
        if (insults.size() == 0)
            return "No insults found, they have either not been added yet or the bot is currently being worked on";
        return insults.get(new Random().nextInt(insults.size()));
    }

    public List<String> getInsults() {
        return insults;
    }
}
