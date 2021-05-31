package org.kurodev.discord.message;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.config.Setting;
import org.kurodev.discord.message.command.generic.admin.CheckSubmissionsCommand;
import org.kurodev.discord.message.command.generic.admin.ExitCommand;
import org.kurodev.discord.message.command.generic.admin.InfoCommand;
import org.kurodev.discord.message.command.generic.admin.ReloadSettingsCommand;
import org.kurodev.discord.message.command.generic.admin.force.ForceAddInsultCommand;
import org.kurodev.discord.message.command.generic.admin.force.ForceAddMemeCommand;
import org.kurodev.discord.message.command.generic.standard.ArchCommand;
import org.kurodev.discord.message.command.guild.standard.*;
import org.kurodev.discord.message.command.guild.standard.rps.RockPaperScissorsCommand;
import org.kurodev.discord.message.command.guild.standard.submission.InsultSubmissionCommand;
import org.kurodev.discord.message.command.guild.standard.submission.MemeSubmissionCommand;
import org.kurodev.discord.message.command.guild.standard.voice.LeaveCommand;
import org.kurodev.discord.message.command.interfaces.Command;
import org.kurodev.discord.message.command.interfaces.Reactable;
import org.kurodev.discord.message.command.quest.Quest;
import org.kurodev.discord.message.command.quest.QuestHandler;
import org.kurodev.discord.util.handlers.TextSampleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

    public void prepare(Runnable additionalShutDownContext) {
        logger.info("initializing commands");
        final TextSampleHandler insults = new TextSampleHandler(Paths.get(Main.SETTINGS.getSetting(Setting.INSULT_FILE)));
        commands.add(new ArchCommand());
        commands.add(new InfoCommand());
        commands.add(new ExitCommand(additionalShutDownContext));
        commands.add(new ForceAddInsultCommand(insults));
        commands.add(new ForceAddMemeCommand());
        commands.add(new CheckSubmissionsCommand());
        commands.add(new ReloadSettingsCommand(commands));
        commands.add(new HelpCommand(commands));
        commands.add(new MemeCommand());
        commands.add(new RandomLineCommand("insult", insults));
        commands.add(new InsultSubmissionCommand(insults));
        commands.add(new MemeSubmissionCommand());
        commands.add(new InspireCommand());
        commands.add(new LeaveCommand());
        commands.add(new RockPaperScissorsCommand());
        commands.add(new VersionCommand());
        commands.add(new ShowActiveQuestsCommand(QUESTS));
        for (Command command : commands) {
            try {
                command.prepare();
            } catch (Exception e) {
                logger.error("Failed to initialize command \"" + command.getCommand() + "\"", e);
            }
        }
        logger.info("initializing commands - DONE");
    }

    public void handle(String command, MessageReceivedEvent event, String[] strArgs) {
        MessageChannel channel = event.getChannel();
        channel.sendTyping().complete();
        for (Command com : commands) {
            if (com.check(command, event)) {
                CommandLineParser parser = new DefaultParser();
                try {
                    CommandLine args = parser.parse(com.getArgs(), strArgs);
                    com.execute(channel, args, event);
                    registerAgainQuest(com, channel, event, args);
                } catch (ParseException e) {
                    channel.sendMessage(e.getMessage()).queue();
                } catch (IOException e) {
                    channel.sendMessage("Oops, something went wrong\n").queue();
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
        boolean exists = QUESTS.exists(event);
        if (exists) {
            QUESTS.get(event).update(event);
        }
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
