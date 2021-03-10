package org.kurodev.discord.command.quest;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This class represents a sort of
 *
 * @author kuro
 **/
public abstract class Quest {
    public static final Consumer<Quest> REFRESH_TIMER_ON_UPDATE = Quest::refresh;
    private final int maxAge;
    private final TimeUnit unit;
    private Consumer<Quest> onFinished;
    private Consumer<Quest> onUpdate;
    private LocalDateTime timeStamp = LocalDateTime.now();

    protected Quest(int maxAge, TimeUnit unit) {
        this.maxAge = maxAge;
        this.unit = unit;
    }

    protected Quest() {
        this(3, TimeUnit.MINUTES);
    }

    public static Quest simpleInstance(Function<GuildMessageReceivedEvent, Boolean> updateQuest) {
        return new Quest() {
            @Override
            protected boolean process(GuildMessageReceivedEvent event) {
                return updateQuest.apply(event);
            }
        };
    }

    public final boolean isExpired() {
        return timeStamp.plus(maxAge, unit.toChronoUnit()).isAfter(LocalDateTime.now());
    }

    public void refresh() {
        timeStamp = LocalDateTime.now();
    }

    /**
     * @return true if the quest is now considered completed
     */
    public boolean update(GuildMessageReceivedEvent event) {
        final boolean result = process(event);
        if (onUpdate != null)
            onUpdate.accept(this);
        if (onFinished != null && result)
            onFinished.accept(this);
        return result;
    }

    /**
     * @param onFinished <code>nullable</code>, Will be invoked whenever {@link #update(GuildMessageReceivedEvent)} would return "true"
     */
    public void setOnFinished(@Nullable Consumer<Quest> onFinished) {
        this.onFinished = onFinished;
    }

    /**
     * @param onUpdate <code>nullable</code>, will be invoked whenever the {@link #update(GuildMessageReceivedEvent)} is called
     */
    public void setOnUpdate(@Nullable Consumer<Quest> onUpdate) {
        this.onUpdate = onUpdate;
    }

    protected abstract boolean process(GuildMessageReceivedEvent event);
}
