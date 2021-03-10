package org.kurodev.discord.command.guild.admin;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.command.Command;
import org.kurodev.discord.command.argument.Argument;
import org.kurodev.discord.command.guild.GuildCommand;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kuro
 **/
public class ReloadSettingsCommand extends AdminCommand {

    private final List<? extends GuildCommand> commands;

    public ReloadSettingsCommand(List<? extends GuildCommand> commands) {
        super("reloadSettings");
        this.commands = commands;
    }

    @Override
    public String getDescription() {
        return "reloads the settings file";
    }

    @Override
    public void execute(TextChannel channel, Argument args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        Message msg = channel.sendMessage("Reloading settings").complete();
        Main.loadSettings();
        List<Command> failed = new LinkedList<>();
        for (GuildCommand command : commands) {
            try {
                command.prepare();
            } catch (Exception e) {
                logger.error("An exception occurred in command {}", command.getCommand(), e);
                failed.add(command);
                commands.remove(command);
            }
        }
        msg.editMessage("Settings reloaded").queue();
        if (!failed.isEmpty()) {
            channel.sendMessage("Failed reloading settings for:\n").append(failed.toString()).queue();
        }
    }
}
