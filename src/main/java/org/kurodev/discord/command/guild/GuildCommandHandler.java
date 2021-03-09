package org.kurodev.discord.command.guild;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.argument.Argument;
import org.kurodev.discord.command.guild.admin.CheckSubmissionsCommand;
import org.kurodev.discord.command.guild.admin.ExitCommand;
import org.kurodev.discord.command.guild.admin.InfoCommand;
import org.kurodev.discord.command.guild.admin.ReloadSettingsCommand;
import org.kurodev.discord.command.guild.force.ForceAddInsultCommand;
import org.kurodev.discord.command.guild.force.ForceAddMemeCommand;
import org.kurodev.discord.command.guild.standard.*;
import org.kurodev.discord.command.guild.standard.rockpaperscissors.RockPaperScissorsCommand;
import org.kurodev.discord.command.guild.submission.InsultSubmissionCommand;
import org.kurodev.discord.command.guild.submission.MemeSubmissionCommand;
import org.kurodev.discord.command.guild.voice.LeaveCommand;
import org.kurodev.discord.command.guild.voice.soundboard.HemanCommand;
import org.kurodev.discord.util.TextSampleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kuro
 **/
public class GuildCommandHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final List<GuildCommand> commands = new ArrayList<>();
    private final TextSampleHandler insults;

    public GuildCommandHandler(TextSampleHandler insults) {
        this.insults = insults;
    }

    public void prepare() {
        logger.info("initializing commands");
        commands.add(new HelpCommand(commands));
        commands.add(new MemeCommand());
        commands.add(new InfoCommand());
        commands.add(new ExitCommand());
        commands.add(new RandomLineCommand("insult", insults));
        commands.add(new InsultSubmissionCommand(insults));
        commands.add(new ForceAddInsultCommand(insults));
        commands.add(new MemeSubmissionCommand());
        commands.add(new ForceAddMemeCommand());
        commands.add(new CheckSubmissionsCommand());
        commands.add(new InspireCommand());
        commands.add(new ReloadSettingsCommand());
        commands.add(new HemanCommand());
        commands.add(new LeaveCommand());
        commands.add(new RockPaperScissorsCommand());
        commands.add(new VersionCommand());

        for (GuildCommand command : commands) {
            try {
                command.prepare();
            } catch (Exception e) {
                logger.error("Failed to initialize command \"" + command.getCommand() + "\"", e);
                commands.remove(command);
            }
        }
        logger.info("initializing commands - DONE");
    }

    public void handle(String command, @NotNull GuildMessageReceivedEvent event, String[] strArgs) {
        TextChannel channel = event.getChannel();
        channel.sendTyping().complete();
        for (GuildCommand com : commands) {
            if (com.check(command, event)) {
                try {
                    Argument args = Argument.parse(strArgs);
                    if (args.hasErrors()) {
                        channel.sendMessage("Oops, something went wrong\n").append(args.getErrorsAsString()).queue();
                    } else {
                        com.execute(channel, args, event);
                    }
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
