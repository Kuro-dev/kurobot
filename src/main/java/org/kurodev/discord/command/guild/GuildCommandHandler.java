package org.kurodev.discord.command.guild;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.argument.Argument;
import org.kurodev.discord.command.guild.admin.CheckSubmissionsCommand;
import org.kurodev.discord.command.guild.admin.ExitCommand;
import org.kurodev.discord.command.guild.admin.InfoCommand;
import org.kurodev.discord.command.guild.admin.ReloadSettingsCommand;
import org.kurodev.discord.command.guild.admin.force.ForceAddInsultCommand;
import org.kurodev.discord.command.guild.admin.force.ForceAddMemeCommand;
import org.kurodev.discord.command.guild.standard.*;
import org.kurodev.discord.command.guild.standard.rps.RockPaperScissorsCommand;
import org.kurodev.discord.command.guild.standard.submission.InsultSubmissionCommand;
import org.kurodev.discord.command.guild.standard.submission.MemeSubmissionCommand;
import org.kurodev.discord.command.guild.standard.voice.LeaveCommand;
import org.kurodev.discord.command.guild.standard.voice.soundboard.HemanCommand;
import org.kurodev.discord.command.quest.Quest;
import org.kurodev.discord.command.quest.QuestHandler;
import org.kurodev.discord.util.handlers.TextSampleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kuro
 **/
public class GuildCommandHandler {
    public static final QuestHandler QUESTS = new QuestHandler();
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
        commands.add(new ReloadSettingsCommand(commands));
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
                        registerAgainQuest(com, channel, event, args);
                    }
                } catch (IOException e) {
                    logger.debug("Exception logged", e);
                }
                return;
            }
        }
        channel.sendMessage("Command is unknown, try using !k help").queue();
        event.getMessage().addReaction("🤷‍♂️").queue();
    }

    private void registerAgainQuest(GuildCommand com, TextChannel channel, @NotNull GuildMessageReceivedEvent trigger, Argument args) {
        Quest q = Quest.simpleInstance(event -> {
            try {
                if ("again".equals(event.getMessage().getContentDisplay()))
                    com.execute(channel, args, trigger);
            } catch (IOException e) {
                channel.sendMessage("Oops, something went wrong\n").append(args.getErrorsAsString()).queue();
            }
            return false;
        });
        q.setOnUpdate(Quest.REFRESH_TIMER_ON_UPDATE);
        QUESTS.register(trigger, q);
    }

    /**
     * @param event The message
     * @return true if there is a quest associated with this user.
     */
    public boolean handleQuests(@NotNull GuildMessageReceivedEvent event) {
        boolean exists = QUESTS.exists(event);
        if (exists) {
            QUESTS.get(event).update(event);
        }
        return exists;
    }
}
