package org.kurodev.discord.message.command.guild.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.message.command.guild.GuildCommand;
import org.kurodev.discord.message.command.guild.voice.handlers.AudioPlayerSendHandler;
import org.kurodev.discord.message.command.guild.voice.handlers.MyAudioResultHandler;
import org.kurodev.discord.message.command.guild.voice.handlers.TrackScheduler;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author kuro
 **/
public abstract class VoiceCommand extends GuildCommand {

    private final AudioPlayerManager playerManager;
    private final AudioPlayerSendHandler sendHandler;
    private final AudioLoadResultHandler resultHandler;

    public VoiceCommand(String command, Permission... neededPermissions) {
        super(command, neededPermissions);
        playerManager = new DefaultAudioPlayerManager();
        AudioPlayer player = playerManager.createPlayer();
        sendHandler = new AudioPlayerSendHandler(player);
        TrackScheduler trackScheduler = new TrackScheduler(player);
        resultHandler = new MyAudioResultHandler(trackScheduler);
        player.addListener(trackScheduler);
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    /**
     * @return the connected voice channel in that guild. Null if not connected to any
     * @param event
     */
    protected VoiceChannel getVoiceChannel(@NotNull MessageReceivedEvent event) {
        User author = event.getAuthor();
        for (VoiceChannel channel : event.getGuild().getVoiceChannels()) {
            for (Member member : channel.getMembers()) {
                if (member.getIdLong() == author.getIdLong()) {
                    return channel;
                }
            }
        }
        return null;
    }

    @Override
    public void execute(TextChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        if (botHasPermission(event)) {
            VoiceChannel voice = getVoiceChannel(event);
            if (voice == null) {
                channel.sendMessage("you must be in a voice channel").queue();
                return;
            }
            AudioManager manager = event.getGuild().getAudioManager();
            manager.setSelfMuted(false);
            executeInternally(channel, args, event);
            manager.setSendingHandler(sendHandler);
            manager.openAudioConnection(voice);
        }
    }

    protected abstract void executeInternally(TextChannel channel, CommandLine args, @NotNull MessageReceivedEvent event);

    protected void loadURL(String url) throws ExecutionException, InterruptedException {
        playerManager.loadItem(url, resultHandler).get();
    }
}
