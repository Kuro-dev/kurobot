package org.kurodev.discord.vote;

public class Score {
    private volatile int upVotes = 0, downVotes = 0;

    public synchronized void addUpvote() {
        upVotes++;
    }

    public synchronized void addDownVote() {
        downVotes++;
    }

    public synchronized void removeDownVote() {
        downVotes--;
    }

    public synchronized void removeUpvote() {
        upVotes--;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public int getScore() {
        return upVotes - downVotes;
    }
}
