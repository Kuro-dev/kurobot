package org.kurodev;

import org.apache.commons.cli.*;
import org.kurodev.config.MySettings;
import org.kurodev.discord.DiscordBot;
import org.kurodev.discord.util.Admins;
import org.kurodev.discord.util.information.DiscordInfoCollector;
import org.kurodev.webserver.WebserverMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
    public static final Admins ADMINS;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Path SETTINGS_FILE = Paths.get("./settings.properties");
    private static final Options ARGUMENTS = new Options();

    static {
        try {
            ADMINS = Admins.Load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ARGUMENTS.addOption("tw", "toggleWebserver", false, "enable the webserver on port 8080");
    }

    public static void main(String[] args) throws LoginException, InterruptedException, IOException, ParseException {

        CommandLineParser parser = new DefaultParser();
        CommandLine arguments = parser.parse(ARGUMENTS, args, true);
        logger.info(TITLE + "-" + VERSION);
        logger.info(getApplicationArgumentsAsString());
        loadSettings();
        startApplication(arguments);
    }

    private static void startApplication(CommandLine args) {
        Runnable shutdown = null;
        if (args.hasOption("-tw")) {
            WebserverMain webserver = new WebserverMain();
            Thread webApp = new Thread(webserver, "Webserver");
            shutdown = webserver::shutdown;
            webApp.setDaemon(true);
            webApp.start();
        }
        DiscordBot bot = new DiscordBot(shutdown);
        DiscordInfoCollector.createInstance(bot);
        Thread discordBot = new Thread(bot, "Discord-bot");
        discordBot.start();
    }

    public static void loadSettings() throws IOException {
        if (Files.exists(SETTINGS_FILE)) {
            logger.info("---------LOADING SETTINGS---------");
            SETTINGS.load(Files.newInputStream(SETTINGS_FILE));
        } else {
            logger.info("---------CREATING NEW DEFAULT SETTINGS---------");
            SETTINGS.restoreDefault();
            saveSettings();
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

    public static String getApplicationArgumentsAsString() {
        final HelpFormatter formatter = new HelpFormatter();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintWriter pw = new PrintWriter(baos, true, StandardCharsets.UTF_8)) {
            formatter.printHelp(pw, 200, "just append them in the end",
                    "All Application arguments:", ARGUMENTS, 2, 5, "");
        }
        return baos.toString(StandardCharsets.UTF_8);
    }
}
