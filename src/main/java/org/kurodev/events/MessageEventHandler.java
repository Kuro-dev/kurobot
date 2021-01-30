package org.kurodev.events;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.UserIDs;
import org.kurodev.command.CommandHandler;
import org.kurodev.config.Setting;

import java.util.Arrays;
import java.util.Random;

/**
 * @author kuro
 **/
public class MessageEventHandler extends ListenerAdapter {
    public static final int INSULT_CHANCE = Integer.parseInt(Main.SETTINGS.getSetting(Setting.INSULT_CHANCE));
    private final InsultHandler insults = new InsultHandler();
    private final CommandHandler commandHandler = new CommandHandler(insults);

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        super.onGuildMessageReceived(event);
        String message = event.getMessage().getContentDisplay();
        if (message.startsWith("!k") || message.startsWith("!K")) {
            String[] split = message.split(" ");
            if (split.length > 1) {
                String command = split[1];
                String[] args = Arrays.copyOfRange(split, 2, split.length);
                commandHandler.handle(command, event, args);
            }
        } else if (event.getAuthor().getId().equals(UserIDs.Mau.getId())) {
            insults.execute(event);
        } else {
            boolean chance = new Random().nextInt(INSULT_CHANCE) == 0;
            if (chance) {
                insults.execute(event);
            }
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
    }

    public void initialize() {
        commandHandler.prepare();
    }
}
