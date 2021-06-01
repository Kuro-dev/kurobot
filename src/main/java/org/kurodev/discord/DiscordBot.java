package org.kurodev.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.kurodev.Main;
import org.kurodev.config.Setting;
import org.kurodev.discord.message.command.MessageEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;


public class DiscordBot implements Runnable {
    private static net.dv8tion.jda.api.JDA JDA;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MessageEventHandler messageEventHandler;

    public DiscordBot(Runnable additionalShutDownContext) {

        messageEventHandler = new MessageEventHandler(additionalShutDownContext);
    }

    public static JDA getJDA() {
        return JDA;
    }

    @Override
    public void run() {
        try {
            JDA = JDABuilder.createDefault(Main.SETTINGS.getSetting(Setting.TOKEN)).build();
            JDA.addEventListener(messageEventHandler);
        } catch (LoginException e) {
            logger.error("Failed to start discord bot", e);
        }
    }

    public MessageEventHandler getMessageEventHandler() {
        return messageEventHandler;
    }
}
