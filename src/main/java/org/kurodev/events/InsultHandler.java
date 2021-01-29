package org.kurodev.events;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.kurodev.Main;
import org.kurodev.conf.Setting;

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
    private static final Path FILE = Paths.get(Main.getSettings().getSetting(Setting.INSULT_FILE));
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
        TextChannel channel = event.getChannel();
        Member member = event.getGuild().getMember(event.getAuthor());
        String insult = getRandomInsult();
        if (member != null) {
            channel.sendMessage(member.getAsMention() + " " + insult.trim()).mention(member).queue();
        } else {
            channel.sendMessage(insult).queue();
        }
    }

    private String getRandomInsult() {
        if (insults.size() == 0)
            return "this would be an insult";
        return insults.get(new Random().nextInt(insults.size()));
    }
}
