package org.kurodev.command.guild.voice;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
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
        return "just makes the bot leave the connected channel";
    }

    @Override
    protected void executeInternally(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) {
        channel.sendMessage("Disconnecting").queue();
        event.getGuild().getAudioManager().closeAudioConnection();
    }
}
