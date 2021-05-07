package org.kurodev.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.command.guild.GuildCommandHandler;
import org.kurodev.discord.command.interfaces.Command;
import org.kurodev.discord.command.privateMsg.PrivateCommandHandler;
import org.kurodev.discord.config.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author kuro
 **/
public class MessageEventHandler extends ListenerAdapter {
    public static final String DELETE_REACTION = "🗑";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final GuildCommandHandler guildCommandHandler = new GuildCommandHandler();
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
        if (message.startsWith(Command.IDENTIFIER) || message.startsWith(Command.IDENTIFIER.toUpperCase())) {
            String[] split = message.split(" ");
            if (split.length > 1) {
                String command = split[1];
                String[] args = Arrays.copyOfRange(split, 2, split.length);
                guildCommandHandler.handle(command, event, args);
            }
        } else {
            guildCommandHandler.handleQuests(event);
        }
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        guildCommandHandler.onGuildMessageReactionAdd(event);
        if (!event.getUser().isBot() && Main.SETTINGS.getSettingBool(Setting.INCLUDE_DELETE_OPTION)) {
            if (event.getReactionEmote().getAsReactionCode().equals(DELETE_REACTION)) {
                try {
                    Message msg = event.getChannel().retrieveMessageById(event.getMessageIdLong()).complete();
                    if (messageAuthorIsThisBot(msg.getAuthor())) {
                        msg.delete().queue();
                    }
                } catch (Exception e) {
                    logger.error("MessageEventHandler encountered a problem: " + e);
                }
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        guildCommandHandler.onGuildMessageReactionRemove(event);
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
        final JDA jda = Main.getJDA();
        initialize();
        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.LISTENING, "!k help"));
        setName(jda);
    }

    private void setName(JDA jda) {
        String newName = Main.SETTINGS.getSetting(Setting.BOT_NAME);
        String currentName = jda.getSelfUser().getName();
        if (!newName.equals(currentName)) {
            logger.info("attempting to change bot name from \"{}\" to \"{}\" ", currentName, newName);
            jda.getSelfUser().getManager().setName(newName).queue();
        }
    }

    public void initialize() {
        guildCommandHandler.prepare();
        privateCommandHandler.prepare();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down bot");
            Main.getJDA().shutdown();
            guildCommandHandler.onShutDown();
            privateCommandHandler.onShutDown();
            logger.info("Shutting down bot - DONE\n-------------------------------");
        }));
    }
}
