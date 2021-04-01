package org.kurodev.discord.command.guild.standard.nasa;

import net.dv8tion.jda.api.Permission;
import org.kurodev.discord.command.guild.GuildCommand;
import org.kurodev.discord.util.UrlRequest;

import java.util.HashMap;
import java.util.Map;

public abstract class NasaCommand extends GuildCommand {
    private static final String APP_KEY = "57EWmxS9vTbvxRLZcrdVhc1Ofn0kOKt8n8QnoxP3";
    private static final String URL = "https://api.nasa.gov/";


    private final UrlRequest request = new UrlRequest();

    public NasaCommand(String command) {
        super(command, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_EMBED_LINKS);
    }

    protected final String sendGetRequest(String urlAddon) {
        return sendGetRequest(urlAddon, new HashMap<>());
    }

    protected final String sendGetRequest(String urlAddon, Map<String, String> params) {
        params.put("api_key", APP_KEY);
        return request.get(URL + urlAddon, params);
    }
}
