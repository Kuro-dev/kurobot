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
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author kuro
 **/
public class Main {
    public static final MySettings SETTINGS = new MySettings();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Path SETTINGS_FILE = Paths.get("./settings.properties");
    private static JDA JDA;

    public static JDA getJDA() {
        return JDA;
    }

    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
        loadSettings();
        startBot();
    }

    private static void startBot() throws InterruptedException, LoginException {
        JDA = JDABuilder.createDefault(SETTINGS.getSetting(Setting.TOKEN)).build();
        MessageEventHandler event = new MessageEventHandler();
        event.initialize();
        JDA.addEventListener(event);
        JDA.awaitReady();
        JDA.getPresence().setActivity(Activity.of(Activity.ActivityType.LISTENING, "!k help"));
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
            OutputStream ous = Files.newOutputStream(SETTINGS_FILE);
            SETTINGS.store(ous, "Bot settings file");
            ous.flush();
            ous.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
