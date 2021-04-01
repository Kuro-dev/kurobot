package org.kurodev.discord.command.guild.standard.voice;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;

/**
 * @author kuro
 **/
public class LeaveCommand extends VoiceCommand {
    public LeaveCommand() {
        super("leave", Permission.VOICE_CONNECT);
    }

    @Override
    public String getDescription() {
        return "just makes the bot leave the voice channel";
    }

    @Override
    protected void executeInternally(TextChannel channel, CommandLine args, @NotNull GuildMessageReceivedEvent event) {
        if (getVoiceChannel(event) == null) {
            channel.sendMessage("Not connected to any voice channels").queue();
        } else {
            channel.sendMessage("Disconnecting").queue();
            event.getGuild().getAudioManager().closeAudioConnection();
        }
    }

    @Override
    protected void prepare(Options args) throws Exception {

    }
}
