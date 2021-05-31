package org.kurodev.discord.message.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.config.Setting;
import org.kurodev.discord.DiscordBot;
import org.kurodev.discord.message.CommandHandler;
import org.kurodev.discord.message.State;
import org.kurodev.discord.message.command.interfaces.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author kuro
 **/
public class MessageEventHandler extends ListenerAdapter {
    public static final String DELETE_REACTION = "🗑";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CommandHandler commandHandler = new CommandHandler();
    private final Runnable additionalShutDownContext;
    private State state = State.OFFLINE;

    public MessageEventHandler(Runnable additionalShutDownContext) {

        this.additionalShutDownContext = additionalShutDownContext;
    }

    private static boolean messageAuthorIsThisBot(User author) {
        return author.getIdLong() == DiscordBot.getJDA().getSelfUser().getIdLong();
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (state != State.RUNNING) {
            return;
        }
        if (event.getAuthor().isBot()) {
            if (messageAuthorIsThisBot(event.getAuthor()) && Main.SETTINGS.getSettingBool(Setting.INCLUDE_DELETE_OPTION)) {
                event.getMessage().addReaction(DELETE_REACTION).queue();
            }
            return;
        }
        handleCommand(event);
    }

    private void handleCommand(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        if (message.startsWith(Command.IDENTIFIER) || message.startsWith(Command.IDENTIFIER.toUpperCase())) {
            String[] split = message.split(" ");
            if (split.length > 1) {
                String command = split[1];
                String[] args = Arrays.copyOfRange(split, 2, split.length);
                commandHandler.handle(command, event, args);
            }
        } else {
            commandHandler.handleQuests(event);
        }
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (state != State.RUNNING) {
            return;
        }
        commandHandler.onMessageReactionAdd(event);
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
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        if (state != State.RUNNING) {
            return;
        }
        commandHandler.onGuildMessageReactionRemove(event);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        final JDA jda = DiscordBot.getJDA();
        initialize();
        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.LISTENING, "!k help"));
        setName(jda);
        setState(State.RUNNING);
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
        setState(State.INITIALIZING);
        commandHandler.prepare(additionalShutDownContext);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            setState(State.SHUTTING_DOWN);
            DiscordBot.getJDA().removeEventListener(this);
            logger.info("Shutting down bot");
            DiscordBot.getJDA().shutdown();
            commandHandler.onShutDown();
            logger.info("Shutting down bot - DONE\n-------------------------------");
        }));
    }

    public State getState() {
        return state;
    }

    private void setState(State newState) {
        state = newState;
    }
}
