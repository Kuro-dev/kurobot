package org.kurodev.command.guild.admin;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;

import java.io.IOException;

/**
 * @author kuro
 **/
public class ReloadSettingsCommand extends AdminCommand {

    public ReloadSettingsCommand() {
        super("reloadSettings");
    }

    @Override
    public String getDescription() {
        return "reloads the settings file";
    }

    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        Message msg = channel.sendMessage("Reloading settings").complete();
        Main.loadSettings();
        msg.editMessage("Settings reloaded").queue();
    }
}
