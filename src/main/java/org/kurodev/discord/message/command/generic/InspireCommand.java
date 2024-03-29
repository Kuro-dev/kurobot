package org.kurodev.discord.message.command.generic;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.message.command.AutoRegister;
import org.kurodev.discord.message.command.generic.GenericCommand;
import org.kurodev.config.Setting;
import org.kurodev.discord.util.UrlRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kuro
 **/
@AutoRegister
public class InspireCommand extends GenericCommand {
    private static final Map<String, String> PARAMS = new HashMap<>();
    private static final String URL = "https://inspirobot.me/api";

    static {
        PARAMS.put("generate", "true");
    }

    private final UrlRequest request = new UrlRequest();

    public InspireCommand() {
        super("inspire");
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        String response = request.get(URL, PARAMS);
        if (response != null) {
            channel.sendMessage(response).queue();
            if (Main.SETTINGS.getSettingBool(Setting.DELETE_COMMAND_MESSAGE))
                event.getMessage().delete().queue();
        } else {
            channel.sendMessage("Something went wrong when requesting the quote\n" +
                    "if this keeps happening please contact the creator of the bot." +
                    " (you can do so by sending a DM to Kuro-bot :)").queue();
        }
    }

    @Override
    public String getDescription() {
        return "will generate a random \"inspirational\" quote and post it in the channel";
    }
}
