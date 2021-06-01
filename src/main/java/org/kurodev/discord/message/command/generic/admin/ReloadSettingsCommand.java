package org.kurodev.discord.message.command.generic.admin;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.message.command.Command;
import org.kurodev.discord.message.command.generic.admin.AdminCommand;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kuro
 **/
public class ReloadSettingsCommand extends AdminCommand {

    private final List<Command> commands;

    public ReloadSettingsCommand(List<Command> commands) {
        super("reloadSettings");
        this.commands = commands;
    }

    @Override
    public String getDescription() {
        return "reloads the settings file and re-prepares every command";
    }


    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        Message msg = channel.sendMessage("Reloading settings").complete();
        Main.loadSettings();
        List<Command> failed = new LinkedList<>();
        for (Command command : commands) {
            try {
                command.prepare();
            } catch (Exception e) {
                logger.error("An exception occurred in command {}", command.getCommand(), e);
                failed.add(command);
                commands.remove(command);
            }
        }
        if (failed.isEmpty()) {
            msg.editMessage("Settings reloaded").queue();
        } else {
            msg.editMessage("Failed reloading settings for:\n").append(failed.toString()).queue();
        }
    }
}
