package org.kurodev.discord.command.guild.voice.soundboard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.argument.Argument;
import org.kurodev.discord.command.guild.CommandArgument;
import org.kurodev.discord.command.guild.voice.VoiceCommand;
import org.kurodev.discord.util.MarkDown;
import org.kurodev.discord.util.UrlRequest;
import org.kurodev.discord.util.cache.Cache;
import org.kurodev.discord.util.cache.SelfUpdatingCache;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author kuro
 **/
public class HemanCommand extends VoiceCommand {
    private static final String LIST_REQUEST_URL = "https://hemann-soundboard.hamsterlabs.de/api/files";
    private static final Map<String, String> searchParams = new HashMap<>();
    @CommandArgument(meaning = "Lists every possible sound that can be played")
    private static final String SHOW_ARGUMENTS = "--list";
    @CommandArgument(requireAdmin = true, meaning = "causes the command to ignore/reload the cache")
    private static final String RELOAD_CACHE = "--noCache";

    static {
        searchParams.put("", "");
    }

    private final Cache<List<JsonFile>> cache = new SelfUpdatingCache<>(3, TimeUnit.DAYS, this::updateCache);

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
    public void execute(TextChannel channel, Argument args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        if (args.getOpt(SHOW_ARGUMENTS)) {
            channel.sendMessage("Here is a full list:\n").append(createList()).queue();
        } else {
            super.execute(channel, args, event);
        }
    }

    @Override
    protected void executeInternally(TextChannel channel, Argument args, @NotNull GuildMessageReceivedEvent event) {
        boolean forceUpdateCache = args.getOpt(RELOAD_CACHE);
        if (forceUpdateCache) {
            logger.info("Force reloading cached files");
        }
        if (cache.isDirty() || forceUpdateCache) {
            updateCache();
        }
        List<String> otherArgs = args.getOtherArgs();
        if (otherArgs.size() > 0) {
            List<JsonFile> list = cache.getCachedItem();
            for (String arg : otherArgs) {
                for (JsonFile jsonFile : list) {
                    if (jsonFile.getSlug().equalsIgnoreCase(arg)) {
                        try {
                            logger.debug("loading resource: {}", jsonFile.toString());
                            loadURL(jsonFile.getUrl());
                        } catch (ExecutionException | InterruptedException e) {
                            logger.error("A (potentially ignorable) error occurred:", e);
                            logger.error("File location: {}", jsonFile.getUrl());
                            channel.sendMessage("Failed to find requested sound: " + jsonFile.getSlug()).queue();
                        }
                    }
                }
            }
        } else {
            channel.sendMessage("argument ").append(MarkDown.CODE.wrap("sound")).append(" required").queue();
        }
    }

    private String createList() {
        StringBuilder out = new StringBuilder("```\n");
        for (JsonFile jsonFile : cache.getCachedItem()) {
            out.append(jsonFile.getSlug()).append("\n");
        }
        return out.append("```").toString();
    }

    private List<JsonFile> updateCache() {
        String jsonList = new UrlRequest().get(LIST_REQUEST_URL, searchParams);
        Type listType = new TypeToken<ArrayList<JsonFile>>() {
        }.getType();
        return new Gson().fromJson(jsonList, listType);
    }
}
