package org.kurodev.discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.command.guild.GuildCommandHandler;
import org.kurodev.discord.command.privateMsg.PrivateCommandHandler;
import org.kurodev.discord.config.Setting;
import org.kurodev.discord.util.handlers.TextSampleHandler;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

/**
 * @author kuro
 **/
public class MessageEventHandler extends ListenerAdapter {
    public static final String DELETE_REACTION = "🗑";
    public static final int INSULT_CHANCE = Integer.parseInt(Main.SETTINGS.getSetting(Setting.INSULT_CHANCE));
    private final TextSampleHandler insults = new TextSampleHandler(Paths.get(Main.SETTINGS.getSetting(Setting.INSULT_FILE)));
    private final GuildCommandHandler guildCommandHandler = new GuildCommandHandler(insults);
    private final PrivateCommandHandler privateCommandHandler = new PrivateCommandHandler();

    private static boolean messageAuthorIsThisBot(User author) {
        return author.getIdLong() == Main.getJDA().getSelfUser().getIdLong();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            if (messageAuthorIsThisBot(event.getAuthor()) && Main.SETTINGS.getSettingBool(Setting.INCLUDE_DELETE_OPTION)) {
                event.getMessage().addReaction(DELETE_REACTION).queue();
            }
            return;
        }
        super.onGuildMessageReceived(event);
        String message = event.getMessage().getContentDisplay();
        if (message.startsWith("!k") || message.startsWith("!K")) {
            String[] split = message.split(" ");
            if (split.length > 1) {
                String command = split[1];
                String[] args = Arrays.copyOfRange(split, 2, split.length);
                guildCommandHandler.handle(command, event, args);
            }
        } else if (event.getAuthor().getIdLong() == UserIDs.MAU.getId()) {
            insults.execute(event);
        } else {
            boolean chance = new Random().nextInt(INSULT_CHANCE) == 0;
            if (chance) {
                insults.execute(event);
            }
        }
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (!event.getUser().isBot() && Main.SETTINGS.getSettingBool(Setting.INCLUDE_DELETE_OPTION)) {
            if (event.getReactionEmote().getAsReactionCode().equals(DELETE_REACTION)) {
                try {
                    Message msg = event.getChannel().retrieveMessageById(event.getMessageIdLong()).complete();
                    if (messageAuthorIsThisBot(msg.getAuthor())) {
                        msg.delete().queue();
                    }
                } catch (Exception e) {
                    System.err.println("MessageEventHandler encountered a problem: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        privateCommandHandler.handle(event);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
    }

    public void initialize() {
        guildCommandHandler.prepare();
        privateCommandHandler.prepare();
    }
}
