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
        channel.sendMessage(response).queue();
    }

    @Override
    public String getDescription() {
        return "will generate a random \"inspirational\" quote and post it in the channel";
    }
}