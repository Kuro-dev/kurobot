package org.kurodev.discord;

import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nullable;

/**
 * A list of important or special userIDs for use in JDA
 *
 * @author kuro
 **/
public enum UserIDs {
    //Admin
    KURO(223878679061725185L),
    ;

    private final long id;

    UserIDs(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Nullable
    public User getUser() {
        User out = DiscordBot.getJDA().getUserById(id); //attempt to find in cache
        if (out == null) {
            out = DiscordBot.getJDA().retrieveUserById(id).complete(); //retrieve from discord DB
        }
        return out;
    }
}
