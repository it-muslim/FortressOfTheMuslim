package jmapps.fortressofthemuslim.Observer;

import java.util.Observable;

public class UpdateBookmarkItems extends Observable {

    private boolean isChecked;
    private static UpdateBookmarkItems instance;

    public static UpdateBookmarkItems getInstance() {
        if (instance == null) {
            instance = new UpdateBookmarkItems();
        }
        return instance;
    }

    public boolean isChecked() {
        return !isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        setChanged();
    }
}