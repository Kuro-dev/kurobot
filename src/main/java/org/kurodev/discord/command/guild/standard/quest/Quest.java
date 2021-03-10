package org.kurodev.discord.command.guild.standard.quest;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author kuro
 **/
public abstract class Quest {
    private final int maxAge;
    private final TimeUnit unit;
    private final LocalDateTime timeStamp = LocalDateTime.now();

    protected Quest(int maxAge, TimeUnit unit) {
        this.maxAge = maxAge;
        this.unit = unit;
    }

    public final boolean isExpired() {
        return timeStamp.plus(maxAge, unit.toChronoUnit()).isAfter(LocalDateTime.now());
    }

    /**
     * @return true if the quest is now considered completed
     */
    public abstract boolean update(GuildMessageReceivedEvent event);
}
