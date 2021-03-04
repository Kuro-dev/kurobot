package org.kurodev.command.guild.voice;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.util.UrlRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kuro
 **/
public class HemanCommand extends VoiceCommand {
    private static final String URL = "https://hemann-soundboard.hamsterlabs.de/audio/";
    private static final String LIST_REQUEST_URL = "";
    private static final Map<String, String> searchParams = new HashMap<>();

    static {
        searchParams.put("", "");
    }

    public HemanCommand() {
        super("heman", Permission.VOICE_CONNECT);
    }

    @Override
    public boolean isListed() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Fun command I implemented simply for use in my programming class.";
    }

    @Override
    protected void executeInternally(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) {
        if (argsContain("-list", args)) {
            String list = new UrlRequest().get(LIST_REQUEST_URL, searchParams);
            if (list != null)
                channel.sendMessage("Here is a list of all commands:\n").append(list).queue();
            else
                channel.sendMessage("Something went wrong when fetching list :(").queue();
        }
        if (args.length > 0) {
            List<String> sounds = fillList(args);
            for (String sound : sounds) {
                try {
                    loadURL(URL + sound);
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
