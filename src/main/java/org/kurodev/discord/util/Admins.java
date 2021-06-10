package org.kurodev.discord.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import net.dv8tion.jda.api.entities.User;
import org.kurodev.discord.DiscordBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Admins {
    private static final Logger logger = LoggerFactory.getLogger(Admins.class);
    private static final Gson JSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    private static final Path FILE = Paths.get("./admins.json");
    @Expose
    private final List<Long> admins = new ArrayList<>();

    public static Admins Load() throws IOException {
        if (Files.exists(FILE)) {
            logger.info("Loading admins");
            byte[] bytes = Files.readAllBytes(FILE);
            return JSON.fromJson(new InputStreamReader(new ByteArrayInputStream(bytes)), Admins.class);
        } else {
            return createDefault();
        }
    }

    private static Admins createDefault() throws IOException {
        logger.info("Creating new admins.json");
        Admins adm = new Admins();
        adm.admins.add(223878679061725185L);
        adm.save();
        return adm;
    }

    public List<Long> getAll() {
        return admins;
    }

    public boolean isAdmin(User user) {
        return isAdmin(user.getIdLong());
    }

    public boolean isAdmin(long id) {
        return admins.stream().anyMatch(aLong -> aLong == id);
    }

    public void save() throws IOException {
        Files.writeString(FILE, JSON.toJson(this));
    }

    public User get(long idLong) {
        for (Long admin : admins) {
            if (admin == idLong) {
                return DiscordBot.getJda().retrieveUserById(admin).complete();
            }
        }
        return null;
    }
}
