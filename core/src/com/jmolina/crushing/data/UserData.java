package com.jmolina.crushing.data;

public class UserData {

    private boolean destroyer;
    private boolean destroyable;

    public UserData() {
        this(false, false);
    }

    public UserData(boolean destroyer, boolean destroyable) {
        this.destroyer = destroyer;
        this.destroyable = destroyable;
    }

    public boolean isDestroyer() {
        return destroyer;
    }

    public boolean isDestroyable() {
        return destroyable;
    }

}
