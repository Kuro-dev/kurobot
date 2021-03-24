package org.kurodev.discord.command.quest;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author kuro
 **/
public class PlayerData {


    private final Guild guild;
    private final TextChannel channel;
    private final User user;

    public PlayerData(Guild guild, TextChannel channel, User user) {

        this.guild = guild;
        this.channel = channel;
        this.user = user;
    }

    public PlayerData(@NotNull GuildMessageReceivedEvent event) {
        this(event.getGuild(), event.getChannel(), event.getAuthor());
    }

    public Guild getGuild() {
        return guild;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public User getUser() {
        return user;
    }

    public boolean matches(@NotNull GuildMessageReceivedEvent event) {
        PlayerData that = new PlayerData(event);
        return guildMatches(that)
                && channelMatches(that)
                && userMatches(that);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerData that = (PlayerData) o;
        return guildMatches(that) && channelMatches(that) && userMatches(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guild.getIdLong(), channel.getIdLong(), user.getIdLong());
    }


    public boolean guildMatches(PlayerData data) {
        return this.guild.getIdLong() == data.guild.getIdLong();
    }

    public boolean userMatches(PlayerData data) {
        return this.user.getIdLong() == data.user.getIdLong();
    }

    public boolean channelMatches(PlayerData data) {
        return this.channel.getIdLong() == data.channel.getIdLong();
    }
}
