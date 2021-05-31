package org.kurodev.discord.message.command.guild.standard.voice.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kuro
 **/
public class MyAudioResultHandler implements AudioLoadResultHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
        logger.error("no matched found");
    }

    @Override
    public void loadFailed(FriendlyException e) {
        e.printStackTrace();
    }
}
