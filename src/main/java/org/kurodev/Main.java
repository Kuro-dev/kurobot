package org.kurodev;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.kurodev.config.MySettings;
import org.kurodev.config.Setting;
import org.kurodev.events.MessageEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author kuro
 **/
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Path SETTINGS_FILE = Paths.get("./settings.properties");
    private static final MySettings SETTINGS = new MySettings();

    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
        loadSettings();
        startBot();
    }

    private static void startBot() throws InterruptedException, LoginException {
        JDA jda = JDABuilder.createDefault(SETTINGS.getSetting(Setting.TOKEN)).build();
        MessageEventHandler event = new MessageEventHandler();
        event.initialize();
        jda.addEventListener(event);
        jda.awaitReady();
        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.LISTENING,"!k help"));
    }

    private static void loadSettings() throws IOException {
        if (Files.exists(SETTINGS_FILE)) {
            logger.info("---------LOADING SETTINGS---------");
            SETTINGS.load(Files.newInputStream(SETTINGS_FILE));
            logger.info("---------------DONE---------------");
        } else {
            logger.info("---------CREATING NEW DEFAULT SETTINGS---------");
            SETTINGS.restoreDefault();
            saveSettings();
            logger.info("--------------------DONE-----------------------");
        }
    }

    public static void saveSettings() {
        try {
            if (!Files.exists(SETTINGS_FILE)) {
                Files.createDirectories(SETTINGS_FILE.getParent());
                Files.createFile(SETTINGS_FILE);
            }
            SETTINGS.store(Files.newOutputStream(SETTINGS_FILE), "Bot settings file");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MySettings getSettings() {
        return SETTINGS;
    }
}
