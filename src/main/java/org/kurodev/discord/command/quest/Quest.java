package org.kurodev.discord.command.quest;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * This class represents a sort of
 *
 * @author kuro
 **/
public abstract class Quest {
    public static final BiConsumer<Quest, GuildMessageReceivedEvent> REFRESH_TIMER_ON_UPDATE =
            (quest, guildMessageReceivedEvent) -> quest.refresh();
    private final int maxAge;
    private final TimeUnit unit;
    protected boolean isFinished = false;
    private String title = "untitled quest";
    /**
     * Always invoked when the quest is finished. Parameter will consist of the currently affected quest, as well as the
     * last event that triggered this quest
     */
    private BiConsumer<Quest, GuildMessageReceivedEvent> onFinished;
    /**
     * @see #onFinished
     */
    private BiConsumer<Quest, GuildMessageReceivedEvent> onUpdate;
    private LocalDateTime timeStamp = LocalDateTime.now();

    /**
     * @param maxAge Maximum age of this quest (default 3)
     * @param unit   Timeunit of the maximum age of the quest (default {@link TimeUnit#MINUTES minutes})
     */
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
        return timeStamp.plus(maxAge, unit.toChronoUnit()).isBefore(LocalDateTime.now());
    }

    public void refresh() {
        timeStamp = LocalDateTime.now();
    }

    /**
     * @return true if the quest is now considered completed
     *
     * @apiNote If this method is invoked while the quest is considered finished, it will simply return true without
     * executing any other code.
     */
    public boolean update(GuildMessageReceivedEvent event) {
        if (!isFinished) {
            isFinished = process(event);
            if (isFinished) {
                if (onFinished != null) {
                    onFinished.accept(this, event);
                }
            } else if (onUpdate != null) {
                onUpdate.accept(this, event);
            }
        }
        return isFinished;
    }

    /**
     * @param onFinished <code>nullable</code>, Will be invoked whenever {@link #update(GuildMessageReceivedEvent)}
     *                   would return "true"
     */
    public void setOnFinished(@Nullable BiConsumer<Quest, GuildMessageReceivedEvent> onFinished) {
        this.onFinished = onFinished;
    }

    /**
     * @param onUpdate <code>nullable</code>, will be invoked whenever the {@link #update(GuildMessageReceivedEvent)}
     *                 is called
     */
    public void setOnUpdate(@Nullable BiConsumer<Quest, GuildMessageReceivedEvent> onUpdate) {
        this.onUpdate = onUpdate;
    }

    /**
     * @param event The message that triggered the given quest
     * @return true if the quest is finished after receiving said message. false if the quest should not be counted as
     * completed yet.
     */
    protected abstract boolean process(GuildMessageReceivedEvent event);

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
