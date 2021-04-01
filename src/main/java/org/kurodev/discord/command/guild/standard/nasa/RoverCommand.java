package org.kurodev.discord.command.guild.standard.nasa;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.guild.standard.nasa.json.RoverImage;
import org.kurodev.discord.command.guild.standard.nasa.json.RoverImages;
import org.kurodev.discord.util.cache.Cache;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RoverCommand extends NasaCommand {
    private static final List<String> ROVERS = Arrays.asList("Curiosity", "Opportunity", "Spirit");
    private static final String ADDON_TEMPLATE = "mars-photos/api/v1/rovers/%s/photos";
    private static final Type ROVER_LIST_TYPE = new TypeToken<ArrayList<RoverImage>>() {
    }.getType();
    private final Map<String, Cache<List<RoverImage>>> caches = new HashMap<>();


    public RoverCommand() {
        super("rover");
    }

    @Override
    protected void prepare(Options args) throws Exception {
        for (String rover : ROVERS) {
            caches.put(rover, new Cache<>(2, TimeUnit.HOURS));
        }
        args.addOption("s", "show-rovers", false, "lists available rovers");
        args.addOption("r", "rover", true, "Selects the rover, default: curiosity");
        args.addOption("d", "show-data", false, "Displays additional data about the rover that took this picture");
        //args.addOption("i","index",true,"give an index for a specific photo");
    }

    @Override
    public void execute(TextChannel channel, CommandLine args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        if (args.hasOption("s")) {
            channel.sendMessage("Available rovers: ").append(ROVERS.toString()).queue();
            return;
        }
        final String rover;
        if (args.hasOption("r")) {
            rover = args.getOptionValue("r");
        } else {
            rover = ROVERS.get(0);
        }
        if (ROVERS.contains(rover)) {
            cleanCaches();
            boolean showData = args.hasOption("d");
            Cache<List<RoverImage>> cache = caches.get(rover);
            final List<RoverImage> photos;
            if (cache.isDirty()) {
                final Map<String, String> data = new HashMap<>();
                data.put("sol", "1000");
                final String response = sendGetRequest(String.format(ADDON_TEMPLATE, rover), data);
                final RoverImages images = new Gson().fromJson(response, RoverImages.class);
                photos = images.getPhotos();
                cache.update(photos);
            } else {
                photos = cache.getCachedItem();
            }
            RoverImage random = photos.get(getRandomImage(photos));
            channel.sendMessage(random.toString()).queue();
        } else {
            channel.sendMessage("Unknown rover: " + rover).queue();
        }

    }

    private int getRandomImage(List<RoverImage> images) {
        return new Random().nextInt(images.size());
    }

    private void cleanCaches() {
        caches.forEach((s, listCache) -> listCache.clean());
    }

    @Override
    public String getDescription() {
        return "A collection of functions regard the nasa Space rovers";
    }
}
