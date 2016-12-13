package com.jmolina.crushing.data;

/**
 * Custom UserData class to flag a body as destroyer or destructible
 */
public class UserData {

    private boolean destroyer;
    private boolean destructible;

    public UserData() {
        this(false, false);
    }

    public UserData(boolean destroyer, boolean destructible) {
        this.destroyer = destroyer;
        this.destructible = destructible;
    }

    public boolean isDestroyer() {
        return destroyer;
    }

    public boolean isDestructible() {
        return destructible;
    }

}
