package org.kurodev.discord.message.quest;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kurodev.discord.message.command.Preparable;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * This class represents a sort of
 *
 * @author kuro
 **/
public abstract class Quest implements Preparable {
    public static final BiConsumer<Quest, MessageReceivedEvent> REFRESH_TIMER_ON_UPDATE =
            (quest, guildMessageReceivedEvent) -> quest.refresh();
    private final int maxAge;
    private final TimeUnit unit;
    protected boolean finished = false;
    private String title = "untitled quest";
    /**
     * Always invoked when the quest is finished. Parameter will consist of the currently affected quest, as well as the
     * last event that triggered this quest
     */
    private BiConsumer<Quest, MessageReceivedEvent> onFinished;
    /**
     * @see #onFinished
     */
    private BiConsumer<Quest, MessageReceivedEvent> onUpdate;
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

    public static Quest simpleInstance(Function<MessageReceivedEvent, Boolean> updateQuest) {
        return new Quest() {
            @Override
            protected boolean process(MessageReceivedEvent event) {
                return updateQuest.apply(event);
            }
        };
    }

    @Override
    public void prepare() {
    }

    public boolean isFinished() {
        return finished;
    }

    public final boolean isExpired() {
        return timeStamp.plus(maxAge, unit.toChronoUnit()).isBefore(LocalDateTime.now());
    }

    public void refresh() {
        timeStamp = LocalDateTime.now();
    }

    /**
     * @param event
     * @return true if the quest is now considered completed
     * @apiNote If this method is invoked while the quest is considered finished, it will simply return true without
     * executing any other code.
     */
    public boolean update(MessageReceivedEvent event) {
        if (!finished) {
            finished = process(event);
            if (finished) {
                if (onFinished != null) {
                    onFinished.accept(this, event);
                }
            } else if (onUpdate != null) {
                onUpdate.accept(this, event);
            }
        }
        return finished;
    }

    /**
     * @param onFinished <code>nullable</code>, Will be invoked whenever {@link #update(MessageReceivedEvent)}
     *                   would return "true"
     */
    public void setOnFinished(@Nullable BiConsumer<Quest, MessageReceivedEvent> onFinished) {
        this.onFinished = onFinished;
    }

    /**
     * @param onUpdate <code>nullable</code>, will be invoked whenever the {@link #update(MessageReceivedEvent)}
     *                 is called
     */
    public void setOnUpdate(@Nullable BiConsumer<Quest, MessageReceivedEvent> onUpdate) {
        this.onUpdate = onUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quest quest = (Quest) o;
        return Objects.equals(title, quest.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    /**
     * @param event The message that triggered the given quest,
     *              will always be in the same channel, by the same user
     * @return true if the quest is finished after receiving said message.
     * false if the quest should not be counted as
     * completed yet.
     */
    protected abstract boolean process(MessageReceivedEvent event);

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
