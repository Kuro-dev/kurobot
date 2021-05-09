package org.kurodev.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.kurodev.Main;
import org.kurodev.discord.config.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Objects;


public class BotMain implements Runnable {
    private static net.dv8tion.jda.api.JDA JDA;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Runnable additionalShutDownContext;

    public BotMain(Runnable additionalShutDownContext) {

        this.additionalShutDownContext = Objects.requireNonNull(additionalShutDownContext);
    }

    public static JDA getJDA() {
        return JDA;
    }

    @Override
    public void run() {
        try {
            JDA = JDABuilder.createDefault(Main.SETTINGS.getSetting(Setting.TOKEN)).build();
            JDA.addEventListener(new MessageEventHandler(additionalShutDownContext));
        } catch (LoginException e) {
            logger.error("Failed to start discord bot", e);
        }
    }
}
