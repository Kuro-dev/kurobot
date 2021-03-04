package org.kurodev.command.guild.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.guild.GuildCommand;
import org.kurodev.command.guild.voice.handlers.AudioPlayerSendHandler;
import org.kurodev.command.guild.voice.handlers.MyAudioResultHandler;
import org.kurodev.command.guild.voice.handlers.TrackScheduler;

/**
 * @author kuro
 **/
public abstract class VoiceCommand extends GuildCommand {

    protected final AudioPlayerManager playerManager;
    protected final AudioPlayer player;
    protected final AudioPlayerSendHandler sendHandler;
    protected final TrackScheduler trackScheduler;
    protected final AudioLoadResultHandler resultHandler;

    public VoiceCommand(String command, Permission... neededPermissions) {
        super(command, neededPermissions);
        playerManager = new DefaultAudioPlayerManager();
        player = playerManager.createPlayer();
        sendHandler = new AudioPlayerSendHandler(player);
        trackScheduler = new TrackScheduler(player);
        resultHandler = new MyAudioResultHandler(trackScheduler);
        player.addListener(trackScheduler);
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    protected VoiceChannel getVoiceChannel(@NotNull GuildMessageReceivedEvent event) {
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
}
