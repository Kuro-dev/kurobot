package org.kurodev.command.guild.voice;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.kurodev.util.UrlRequest;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kuro
 **/
@SuppressWarnings("ResultOfMethodCallIgnored")
public class HemanCommand extends VoiceCommand {
    private static final String URL = "https://hemann-soundboard.hamsterlabs.de/audio/";
    private static final UrlRequest REQUEST = new UrlRequest();

    public HemanCommand() {
        super("heman", Permission.VOICE_CONNECT);
    }

    @Override
    public void prepare() throws Exception {

    }

    @Override
    public String getDescription() {
        return "Fun command I implemented simply for use in my programming class.";
    }

    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        if (botHasPermission(event)) {
            VoiceChannel voice = getVoiceChannel(event);
            if (voice == null) {
                channel.sendMessage("you must be in a voice channel").queue();
                return;
            }
            if (args.length > 0) {
                List<String> sounds = fillList(args);
                AudioManager manager = event.getGuild().getAudioManager();
                manager.setSelfMuted(false);
                for (String sound : sounds) {
                    try {
                        playerManager.loadItem(URL + sound, resultHandler).get();
                    } catch (Exception e) {
                        logger.error("A (potentially ignorable) error occurred:", e);
                        channel.sendMessage("Failed to find requested sound: " + sound).queue();
                    }
                }
                manager.setSendingHandler(sendHandler);
                manager.openAudioConnection(voice);
            } else {
                channel.sendMessage("argument `sound` required").queue();
            }
        } else {
            MessageAction action = channel.sendMessage("Missing permissions.\npermissions needed: ");
            for (Permission permission : neededPermissions) {
                action.append(permission.getName()).append("\n");
            }
            action.queue();
        }
    }

    private InputStream getSound(String sound) throws Exception {
        HttpsURLConnection connection = REQUEST.getHttpsClient(URL + sound);
        return connection.getInputStream();
    }

    private List<String> fillList(String[] args) {
        return Arrays.stream(args).map(arg -> arg + ".mp3").collect(Collectors.toList());
    }
}
