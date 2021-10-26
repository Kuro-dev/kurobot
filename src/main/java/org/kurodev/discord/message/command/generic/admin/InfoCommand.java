package org.kurodev.discord.message.command.generic.admin;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.config.Setting;
import org.kurodev.discord.message.command.AutoRegister;

import java.io.IOException;

/**
 * @author kuro
 **/
@AutoRegister
public class InfoCommand extends AdminCommand {
    public InfoCommand() {
        super("Info");
    }

    @Override
    public String getDescription() {
        return "Returns the current settings configuration of the bot";
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        MessageAction msg = channel.sendMessage("\nSystem Information:\n");
        msg.append("Arch: ").append(System.getProperty("os.name")).append("\n")
                .append(Main.TITLE + " version: " + Main.VERSION)
                .append("\n--------------------------------\n").append("These are the settings:```\n");
        for (Setting value : Setting.values()) {
            if (!"token".equals(value.getKey()))
                msg.append(value.getKey()).append(" = ").append(Main.SETTINGS.getSetting(value)).append("\n");
        }
        msg.append("```").queue();
    }
}
