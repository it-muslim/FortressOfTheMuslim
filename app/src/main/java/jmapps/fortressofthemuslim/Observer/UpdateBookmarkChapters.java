package jmapps.fortressofthemuslim.Observer;

import java.util.Observable;

public class UpdateBookmarkChapters extends Observable {

    private boolean isChecked;
    private static UpdateBookmarkChapters instance;

    public static UpdateBookmarkChapters getInstance() {
        if (instance == null) {
            instance = new UpdateBookmarkChapters();
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