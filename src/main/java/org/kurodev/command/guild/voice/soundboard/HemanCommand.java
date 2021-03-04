package org.kurodev.command.guild.voice.soundboard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.guild.voice.VoiceCommand;
import org.kurodev.util.cache.Cache;
import org.kurodev.util.UrlRequest;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;
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

    private final Cache<List<JsonFile>> fileCache = new Cache<>(3, TimeUnit.DAYS);

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
        if (fileCache.isDirty()) {
            updateCache();
        }
        if (argsContain("-list", args)) {
            channel.sendMessage("Here is a full list:\n").append(createList()).queue();
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

    private String createList() {
        StringBuilder out = new StringBuilder("```");
        for (JsonFile jsonFile : fileCache.getCachedItem()) {
            out.append(jsonFile.getSlug()).append("\n");
        }
        return out.append("```").toString();
    }

    private void updateCache() {
        String jsonList = new UrlRequest().get(LIST_REQUEST_URL, searchParams);
        Type listType = new TypeToken<ArrayList<JsonFile>>() {
        }.getType();
        List<JsonFile> files = new Gson().fromJson(jsonList, listType);
        fileCache.update(files);
    }

    private List<String> fillList(String[] args) {
        return Arrays.stream(args).map(arg -> arg + ".mp3").collect(Collectors.toList());
    }
}
