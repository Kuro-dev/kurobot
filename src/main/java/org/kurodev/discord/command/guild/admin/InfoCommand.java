package org.kurodev.discord.command.guild.admin;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.config.Setting;

/**
 * @author kuro
 **/
public class InfoCommand extends AdminCommand {
    public InfoCommand() {
        super("Info");
    }


    @Override
    protected void prepare(Options args) throws Exception {

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void execute(TextChannel channel, CommandLine args, @NotNull GuildMessageReceivedEvent event) {
        MessageAction msg = channel.sendMessage("```\nThese are the settings:\n");
        for (Setting value : Setting.values()) {
            if (!"token".equals(value.getKey()))
                msg.append(value.getKey()).append(" = ").append(Main.SETTINGS.getSetting(value)).append("\n");
        }
        msg.append("```").queue();
    }

    @Override
    public String getDescription() {
        return "Returns the current settings configuration of the bot";
    }
}
