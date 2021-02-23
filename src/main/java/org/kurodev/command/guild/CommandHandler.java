package org.kurodev.command.guild;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.guild.admin.CheckSubmissionsCommand;
import org.kurodev.command.guild.admin.ExitCommand;
import org.kurodev.command.guild.force.ForceAddInsultCommand;
import org.kurodev.command.guild.force.ForceAddMemeCommand;
import org.kurodev.command.guild.standard.HelpCommand;
import org.kurodev.command.guild.standard.InfoCommand;
import org.kurodev.command.guild.standard.InsultCommand;
import org.kurodev.command.guild.standard.MemeCommand;
import org.kurodev.command.guild.submission.InsultSubmissionCommand;
import org.kurodev.command.guild.submission.MemeSubmissionCommand;
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
    private final List<GuildCommand> commands = new ArrayList<>();
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
        commands.add(new ForceAddMemeCommand());
        commands.add(new ForceAddInsultCommand(insults));
        commands.add(new CheckSubmissionsCommand());

        for (GuildCommand command : commands) {
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
        for (GuildCommand com : commands) {
            if (com.check(command, event)) {
                try {
                    com.execute(channel, args, event);
                } catch (IOException e) {
                    logger.debug(this.getClass().getSimpleName() + "#handle() exception logged", e);
                    logger.debug("Commandhandler#handle() exception logged", e);
                }
                return;
            }
        }
        channel.sendMessage("Command is unknown, try using !k help").queue();
        event.getMessage().addReaction("🤷‍♂️").queue();
    }
}
