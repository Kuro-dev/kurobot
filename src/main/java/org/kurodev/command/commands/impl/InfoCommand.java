package org.kurodev.command.commands.impl;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.command.commands.Command;
import org.kurodev.conf.Setting;

/**
 * @author kuro
 **/
public class InfoCommand extends Command {
    public InfoCommand() {
        super("info");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) {
        MessageAction msg = channel.sendMessage("These are the settings:\n");
        for (Setting value : Setting.values()) {
            msg.append(value.getKey()).append("=").append(Main.getSettings().getSetting(value)).append("\n");
        }
        msg.queue();
    }
}
