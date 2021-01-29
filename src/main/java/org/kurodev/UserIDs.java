package org.kurodev;

/**
 * @author kuro
 **/
public enum UserIDs {
    kuro("223878679061725185"),
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
