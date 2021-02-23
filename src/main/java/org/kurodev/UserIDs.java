package org.kurodev;

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
    //some guy I just want to insult everytime he writes something.
    MAU(290501224455995392L),
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
        return Main.getJDA().getUserById(id);
    }
}
