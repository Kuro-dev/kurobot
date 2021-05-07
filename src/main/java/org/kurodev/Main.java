package org.kurodev;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.kurodev.discord.MessageEventHandler;
import org.kurodev.discord.config.MySettings;
import org.kurodev.discord.config.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.Manifest;

/**
 * @author kuro
 **/
public class Main {
    public static final MySettings SETTINGS = new MySettings();
    public static final String TITLE = getFromManifest("Implementation-Title");
    public static final String VERSION = getVersion();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Path SETTINGS_FILE = Paths.get("./settings.properties");
    private static JDA JDA;

    public static JDA getJDA() {
        return JDA;
    }

    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
        logger.info(TITLE + " - ver " + VERSION);
        loadSettings();
        startBot();
    }

    private static void startBot() throws LoginException {
        JDA = JDABuilder.createDefault(SETTINGS.getSetting(Setting.TOKEN)).build();
        JDA.addEventListener(new MessageEventHandler());
    }

    public static void loadSettings() throws IOException {
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

    public static String getVersion() {
        String result = getFromManifest("Implementation-Version");
        return result == null ? "no version found" : result;
    }

    public static String getFromManifest(String find) {
        try {
            Enumeration<URL> resources = Main.class.getClassLoader()
                    .getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                try {
                    Manifest manifest = new Manifest(resources.nextElement().openStream());
                    // check that this is your manifest and do what you need or get the next one
                    return manifest.getMainAttributes().getValue(find);
                } catch (IOException E) {
                    // handle
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
