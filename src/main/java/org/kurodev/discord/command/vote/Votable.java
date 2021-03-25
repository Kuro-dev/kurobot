package org.kurodev.discord.command.vote;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

public interface Votable<T> {
    String UPVOTE = "👍";
    String DOWN_VOTE = "👎";

    void upvote(T item);

    void removeUpvote(T item);

    void downVote(T item);

    void removeDownVote(T item);

    int getScore(T item);

    default boolean canVote(GuildMessageReactionAddEvent event) {
        System.out.println(event.getReaction().getReactionEmote().getAsReactionCode());
        return false;
    }

    default boolean canVote(GuildMessageReactionRemoveEvent event) {
        System.out.println(event.getReaction().getReactionEmote().getAsReactionCode());
        return false;
    }

    default void makeVoteable(Message message) {
        message.addReaction(UPVOTE).and(message.addReaction(DOWN_VOTE)).queue();
    }

}
