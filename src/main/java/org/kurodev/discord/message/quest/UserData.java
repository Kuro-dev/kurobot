package org.kurodev.discord.message.quest;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author kuro
 **/
public class UserData {


    private final Guild guild;
    private final MessageChannel channel;
    private final User user;

    public UserData(Guild guild, MessageChannel channel, User user) {

        this.guild = guild;
        this.channel = channel;
        this.user = user;
    }

    public static UserData of(MessageReceivedEvent event) {
        Guild guild = null;
        if (event.isFromGuild()) {
            guild = event.getGuild();
        }
        return new UserData(guild, event.getChannel(), event.getAuthor());
    }

    public Guild getGuild() {
        return guild;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public User getUser() {
        return user;
    }

    public boolean matches(@NotNull MessageReceivedEvent event) {
        UserData that = UserData.of(event);
        return guildMatches(that)
                && channelMatches(that)
                && userMatches(that);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData that = (UserData) o;
        return guildMatches(that) && channelMatches(that) && userMatches(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guild, channel.getIdLong(), user.getIdLong());
    }


    public boolean guildMatches(UserData data) {
        if (data.guild == null ^ guild == null)
            return false;

        if (data.guild == null)
            return true;

        return this.guild.getIdLong() == data.guild.getIdLong();
    }

    public boolean userMatches(UserData data) {
        return this.user.getIdLong() == data.user.getIdLong();
    }

    public boolean channelMatches(UserData data) {
        return this.channel.getIdLong() == data.channel.getIdLong();
    }
}
