package org.kurodev.discord.command.guild.voice;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.argument.Argument;

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
    protected void executeInternally(TextChannel channel, Argument args, @NotNull GuildMessageReceivedEvent event) {
        if (getVoiceChannel(event) == null) {
            channel.sendMessage("Not connected to any voice channels").queue();
        } else {
            channel.sendMessage("Disconnecting").queue();
            event.getGuild().getAudioManager().closeAudioConnection();
        }
    }
}
