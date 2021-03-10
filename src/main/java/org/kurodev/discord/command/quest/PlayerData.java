package org.kurodev.discord.command.quest;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author kuro
 **/
public class PlayerData {
    private final long guildId;
    private final long channelId;
    private final long userId;

    public PlayerData(long guildId, long channelId, long userId) {
        this.guildId = guildId;
        this.channelId = channelId;
        this.userId = userId;
    }

    public PlayerData(@NotNull GuildMessageReceivedEvent event) {
        this(event.getGuild().getIdLong(), event.getChannel().getIdLong(), event.getAuthor().getIdLong());
    }

    public boolean matches(@NotNull GuildMessageReceivedEvent event) {
        return guildId == event.getGuild().getIdLong()
                && channelId == event.getChannel().getIdLong()
                && userId == event.getAuthor().getIdLong();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerData that = (PlayerData) o;
        return guildId == that.guildId && channelId == that.channelId && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guildId, channelId, userId);
    }
}
