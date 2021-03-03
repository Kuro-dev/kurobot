package org.kurodev.command.guild.standard;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.guild.GuildCommand;
import org.kurodev.util.UrlRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kuro
 **/
public class InspireCommand extends GuildCommand {
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
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        String response = request.get(URL, PARAMS);
        if (response != null) {
            channel.sendMessage(response).queue();
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
