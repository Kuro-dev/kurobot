package org.kurodev.discord.vote.impl;

import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.vote.Score;
import org.kurodev.discord.vote.Votable;

import java.util.HashMap;
import java.util.Map;

public class ReactionVote<T> implements Votable<T> {
    private final Map<T, Score> votes = new HashMap<>();

    public @NotNull Score get(T item) {
        Score vote = votes.get(item);
        if (vote != null) {
            return vote;
        }
        vote = new Score();
        votes.put(item, vote);
        return vote;
    }

    @Override
    public void upvote(T item) {
        get(item).addUpvote();
    }

    @Override
    public void removeUpvote(T item) {
        get(item).removeUpvote();
    }

    @Override
    public void downVote(T item) {
        get(item).addDownVote();
    }

    @Override
    public void removeDownVote(T item) {
        get(item).removeDownVote();
    }

    @Override
    public int getScore(T item) {
        return get(item).getScore();
    }

    public void handleReaction(String reaction, T item, boolean isAdded) {
        final Score score = get(item);
        if (UPVOTE.equals(reaction)) {
            if (isAdded) {
                score.addUpvote();
            } else {
                score.removeUpvote();
            }
        } else if (DOWN_VOTE.equals(reaction)) {
            if (isAdded) {
                score.addDownVote();
            } else {
                score.removeDownVote();
            }
        }
    }
}

