package jmapps.fortressofthemuslim.Player;

public interface MainContract {

    interface MainView {
        void playState(boolean isChecked);
    }

    interface Presenter {
        void play(boolean isChecked);
    }
}