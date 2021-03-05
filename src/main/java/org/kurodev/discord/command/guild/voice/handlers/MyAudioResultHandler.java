package org.kurodev.discord.command.guild.voice.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

/**
 * @author kuro
 **/
public class MyAudioResultHandler implements AudioLoadResultHandler {
    private final TrackScheduler trackScheduler;

    public MyAudioResultHandler(TrackScheduler trackScheduler) {

        this.trackScheduler = trackScheduler;
    }


    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        trackScheduler.queue(audioTrack);
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {

    }

    @Override
    public void noMatches() {
        System.err.println("no matches found");
    }

    @Override
    public void loadFailed(FriendlyException e) {
        e.printStackTrace();
    }
}
