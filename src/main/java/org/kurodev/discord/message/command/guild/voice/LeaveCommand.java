package org.kurodev.discord.message.command.guild.voice;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.message.command.AutoRegister;

/**
 * @author kuro
 **/
@AutoRegister(load = false)
public class LeaveCommand extends VoiceCommand {
    public LeaveCommand() {
        super("leave", Permission.VOICE_CONNECT);
    }

    @Override
    public String getDescription() {
        return "just makes the bot leave the voice channel";
    }

    @Override
    protected void executeInternally(TextChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) {
        if (getVoiceChannel(event) == null || getVoiceChannel(event).getMembers().contains(event.getGuild().getSelfMember())) {
            channel.sendMessage("Not connected to any voice channels").queue();
        } else {
            channel.sendMessage("Disconnecting").queue();
            event.getGuild().getAudioManager().closeAudioConnection();
        }
    }
}
