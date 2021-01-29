package org.kurodev;

/**
 * A list of important or special userIDs for use in JDA
 * @author kuro
 **/
public enum UserIDs {
    //Admin
    kuro("223878679061725185"),
    //some guy I just want to insult everytime he writes something.
    Mau("290501224455995392"),
    ;

    private final String id;

    UserIDs(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
