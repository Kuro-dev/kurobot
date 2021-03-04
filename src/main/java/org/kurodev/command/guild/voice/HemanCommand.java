package org.kurodev.command.guild.voice;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.util.UrlRequest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kuro
 **/
@SuppressWarnings("ResultOfMethodCallIgnored")
public class HemanCommand extends VoiceCommand {
    private static final String URL = "https://hemann-soundboard.hamsterlabs.de/audio/";
    private static final UrlRequest REQUEST = new UrlRequest();

    public HemanCommand() {
        super("heman", Permission.VOICE_CONNECT);
    }

    @Override
    public String getDescription() {
        return "Fun command I implemented simply for use in my programming class.";
    }

    @Override
    protected void executeInternally(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) {
        if (args.length > 0) {
            List<String> sounds = fillList(args);
            for (String sound : sounds) {
                try {
                    loadURL(event, URL + sound);
                } catch (Exception e) {
                    logger.error("A (potentially ignorable) error occurred:", e);
                    channel.sendMessage("Failed to find requested sound: " + sound).queue();
                }
            }
        } else {
            channel.sendMessage("argument `sound` required").queue();
        }
    }

    private List<String> fillList(String[] args) {
        return Arrays.stream(args).map(arg -> arg + ".mp3").collect(Collectors.toList());
    }
}
