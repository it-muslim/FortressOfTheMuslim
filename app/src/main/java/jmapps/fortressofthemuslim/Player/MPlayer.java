package jmapps.fortressofthemuslim.Player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import jmapps.fortressofthemuslim.Model.ChaptersContentModel;

public class MPlayer {

    private Context mContext;
    private List<ChaptersContentModel> mChaptersContentModel;
    private MediaPlayer mediaPlayer;

    public MPlayer(Context context, List<ChaptersContentModel> chaptersContentModel) {
        this.mContext = context;
        this.mChaptersContentModel = chaptersContentModel;
    }

    public boolean setPlay(boolean isChecked, String idPosition) {
        if (isChecked) {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            } else {
                play(idPosition);
            }
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        }
        return isChecked;
    }

    private void play(String idPosition) {
        clearPlayer();

        boolean checkAudio = new File(Environment.getExternalStorageDirectory() +
                File.separator + "FortressOfTheMuslim_audio" + File.separator + "dua" + idPosition + ".mp3").exists();

        if (checkAudio) {

            File pathToAudio = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "FortressOfTheMuslim_audio" + File.separator + "dua" + idPosition + ".mp3");
            mediaPlayer = MediaPlayer.create(mContext, Uri.fromFile(pathToAudio));
            mediaPlayer.start();

        } else {
            Toast.makeText(mContext, "Файл не загружен или удален", Toast.LENGTH_SHORT).show();
        }

    }

    private void clearPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
