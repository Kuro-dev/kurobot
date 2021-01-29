package org.kurodev.command;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.admin.ExitCommand;
import org.kurodev.command.standard.Command;
import org.kurodev.command.standard.impl.HelpCommand;
import org.kurodev.command.standard.impl.InfoCommand;
import org.kurodev.command.standard.impl.InsultCommand;
import org.kurodev.command.standard.impl.MemeCommand;
import org.kurodev.command.submission.InsultSubmissionCommand;
import org.kurodev.command.submission.MemeSubmissionCommand;
import org.kurodev.events.InsultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kuro
 **/
public class CommandHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final List<Command> commands = new ArrayList<>();
    private final InsultHandler insults;

    public CommandHandler(InsultHandler insults) {
        this.insults = insults;
    }

    public void prepare() {
        logger.info("initializing commands");
        commands.add(new HelpCommand(commands));
        commands.add(new MemeCommand());
        commands.add(new InfoCommand());
        commands.add(new ExitCommand());
        commands.add(new InsultCommand(insults));
        commands.add(new MemeSubmissionCommand());
        commands.add(new InsultSubmissionCommand(insults));

        for (Command command : commands) {
            try {
                command.prepare();
            } catch (Exception e) {
                logger.info("Failed to initialize command \"" + command.getCommand() + "\"", e);
                commands.remove(command);
            }
        }
        logger.info("initializing commands - DONE");
    }

    public void handle(String command, @NotNull GuildMessageReceivedEvent event, String[] args) {
        TextChannel channel = event.getChannel();
        channel.sendTyping().complete();
        for (Command com : commands) {
            if (com.check(command, event)) {
                try {
                    com.execute(channel, args, event);
                } catch (IOException e) {
                    channel.sendMessage("something went wrong: " + e.getMessage()).queue();
                    for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                        logger.debug(stackTraceElement.toString());
                    }
                }
                return;
            }
        }
        channel.sendMessage("command is unknown, try using !k help").queue();
        event.getMessage().addReaction("🤷‍♂️").queue();
    }
}
