package org.kurodev.discord.message;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.config.Setting;
import org.kurodev.discord.message.command.AutoRegister;
import org.kurodev.discord.message.command.Command;
import org.kurodev.discord.message.command.Reactable;
import org.kurodev.discord.message.command.enums.CommandState;
import org.kurodev.discord.message.command.generic.HelpCommand;
import org.kurodev.discord.message.command.generic.ShowActiveQuestsCommand;
import org.kurodev.discord.message.command.generic.admin.ExitCommand;
import org.kurodev.discord.message.command.generic.admin.ReloadSettingsCommand;
import org.kurodev.discord.message.command.generic.admin.RestartComputerCommand;
import org.kurodev.discord.message.command.generic.admin.force.ForceAddInsultCommand;
import org.kurodev.discord.message.command.generic.submission.InsultSubmissionCommand;
import org.kurodev.discord.message.command.guild.RandomLineCommand;
import org.kurodev.discord.message.quest.Quest;
import org.kurodev.discord.message.quest.QuestHandler;
import org.kurodev.discord.util.handlers.TextSampleHandler;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kuro
 **/
public class CommandHandler {
    public static final QuestHandler QUESTS = new QuestHandler();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final List<Command> commands = new ArrayList<>();


    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Instantiates all Commands that need additional Context and do not have a default Constructor.
     */
    public void prepare(Runnable additionalShutDownContext) {
        logger.info("initializing commands");
        final TextSampleHandler insults = new TextSampleHandler(Paths.get(Main.SETTINGS.getSetting(Setting.INSULT_FILE)));
        commands.add(new ExitCommand(additionalShutDownContext));
        commands.add(new RestartComputerCommand(additionalShutDownContext));
        commands.add(new ForceAddInsultCommand(insults));
        commands.add(new ReloadSettingsCommand(commands));
        commands.add(new HelpCommand(commands));
        commands.add(new RandomLineCommand("insult", insults));
        commands.add(new InsultSubmissionCommand(insults));
        commands.add(new ShowActiveQuestsCommand(QUESTS));
        loadAutoRegisteredCommands();
        logger.info("Loaded a total of {} commands", commands.size());
        initialize();
    }

    /**
     * Uses Reflection magic to automatically load up Commands with {@link AutoRegister} annotation
     */
    private void loadAutoRegisteredCommands() {
        int autowired = 0;
        logger.info("Loading autowired commands");
        Reflections reflections = new Reflections("org.kurodev.discord.message.command");
        var commands = reflections.getSubTypesOf(Command.class);
        for (Class<? extends Command> com : commands) {
            if (com.isAnnotationPresent(AutoRegister.class)) {
                if (com.getAnnotationsByType(AutoRegister.class)[0].load())
                    for (Constructor<?> constructor : com.getConstructors()) {
                        if (constructor.getParameterCount() == 0) {
                            try {
                                Command obj = (Command) constructor.newInstance();
                                boolean anyMatch = this.commands.stream().anyMatch(command -> command.getClass() == obj.getClass());
                                if (!anyMatch) {
                                    this.commands.add(obj);
                                    autowired++;
                                } else {
                                    logger.info("autowired command {} was already registered.", obj);
                                }
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                                logger.error("Failed to instantiate \"" + com + "\"", e);
                            }
                        }
                    }
            }
        }
        logger.info("Autowired {} commands", autowired);
    }

    private void initialize() {
        logger.info("Initializing Commands");
        for (Command command : commands) {
            command.prepare();
        }
        List<Command> failed = commands.stream()
                .filter(command -> command.getState() == CommandState.FAILED)
                .collect(Collectors.toList());
        logger.info("Successfully initialized {} commands", commands.size() - failed.size());
        if (!failed.isEmpty()) {
            logger.warn("Failed to initialize the following commands:");
            for (Command command : failed) {
                logger.warn(command.getCommand());
            }
        }
    }

    public void handle(String command, MessageReceivedEvent event, String[] strArgs) {
        MessageChannel channel = event.getChannel();
        channel.sendTyping().complete();
        for (Command com : commands) {
            if (com.check(command, event)) {
                CommandLineParser parser = new DefaultParser();
                try {
                    CommandLine args = parser.parse(com.getOptions(), strArgs, true);
                    com.execute(channel, args, event);
                    registerAgainQuest(com, channel, event, args);
                } catch (Throwable e) {
                    channel.sendMessage(e.getMessage()).queue();
                    logger.error("something went wrong in command {}", com.getCommand(), e);
                }
                return;
            }
        }
        channel.sendMessage("Command is unknown, try using !k help").queue();
        event.getMessage().addReaction("🤷‍♂️").queue();
    }

    private void registerAgainQuest(Command com, MessageChannel channel, MessageReceivedEvent
            trigger, CommandLine args) {
        Quest q = Quest.simpleInstance(event -> {
            try {
                if ("again".equals(event.getMessage().getContentDisplay()))
                    com.execute(channel, args, trigger);
            } catch (IOException e) {
                channel.sendMessage("Oops, something went wrong\n").queue();
            }
            return false;
        });
        q.setTitle("\"again\" repeat quest");
        q.setOnUpdate(Quest.REFRESH_TIMER_ON_UPDATE);
        QUESTS.register(trigger, q);
    }

    /**
     * @param event The message
     */
    public void handleQuests(MessageReceivedEvent event) {
        if (hasQuest(event)) {
            QUESTS.get(event).forEach(quest -> quest.update(event));
        }
    }

    public boolean hasQuest(MessageReceivedEvent event) {
        return QUESTS.exists(event);
    }

    public void onShutDown() {
        for (Command command : commands) {
            try {
                command.onShutdown();
            } catch (Exception e) {
                logger.error("Failed to execute shutdown for {}", command);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        List<Command> commands = this.commands.stream().filter(Command::hasReactAction).collect(Collectors.toList());
        event.getUser();
        if (!event.getUser().isBot()) {
            event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(message -> {
                for (Command command : commands) {
                    ((Reactable) command).onReact(message, event);
                }
            });
        }
    }

    public void onGuildMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        List<Command> commands = this.commands.stream().filter(Command::hasReactAction).collect(Collectors.toList());
        event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(message -> {
            for (Command command : commands) {
                ((Reactable) command).onReact(message, event);
            }
        });
    }
}
