package jmapps.fortressofthemuslim;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import jmapps.fortressofthemuslim.Adapter.ChaptersContentAdapter;
import jmapps.fortressofthemuslim.DBSetup.DBAssetHelper;
import jmapps.fortressofthemuslim.DBSetup.SQLChapterContentList;
import jmapps.fortressofthemuslim.Download.DownloadsAudio;
import jmapps.fortressofthemuslim.Model.ChaptersContentModel;
import jmapps.fortressofthemuslim.Observer.UpdateBookmarkChapters;
import jmapps.fortressofthemuslim.Observer.UpdateBookmarkItems;
import jmapps.fortressofthemuslim.Permission.GetPermission;
import jmapps.fortressofthemuslim.ViewHolder.ChaptersContentVH;

import static jmapps.fortressofthemuslim.Fragment.MainChapters.keyChapterBookmark;
import static jmapps.fortressofthemuslim.Fragment.MainChapters.keyChapterName;
import static jmapps.fortressofthemuslim.Fragment.MainChapters.keyChapterPosition;
import static jmapps.fortressofthemuslim.Fragment.MainItemsAll.keyItemBookmark;

public class ChapterContentActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,
        Observer, ChaptersContentAdapter.SupplicationItemButtons {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private MediaPlayer mediaPlayer;

    private UpdateBookmarkChapters updateBookmarkChapters;
    private UpdateBookmarkItems updateBookmarkItems;

    private Toolbar toolbar;
    private MenuItem itemDownloadChapter;
    private List<ChaptersContentModel> chaptersContent;
    private ChaptersContentAdapter chaptersContentAdapter;
    private DownloadsAudio downloadsAudio;

    private String chapterPosition;
    private int showTargetTap = 1;

    @Override
    protected void onStart() {
        super.onStart();
        updateBookmarkChapters = UpdateBookmarkChapters.getInstance();
        updateBookmarkChapters.addObserver(this);

        updateBookmarkItems = UpdateBookmarkItems.getInstance();
        updateBookmarkItems.addObserver(this);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_content);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DBAssetHelper dbAssetHelper = new DBAssetHelper(this);
        dbAssetHelper.getReadableDatabase();

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        chapterPosition = getIntent().getStringExtra(keyChapterPosition);
        String chapterName = getIntent().getStringExtra(keyChapterName);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ToggleButton tbAddRemoverBookmarkChapter = findViewById(R.id.tb_add_remove_bookmark_chapter);
        RecyclerView rvChapterContent = findViewById(R.id.rv_chapter_content);
        TextView tvChapterName = findViewById(R.id.tv_chapter_name);
        tvChapterName.setText(Html.fromHtml(chapterName));

        tbAddRemoverBookmarkChapter.setOnCheckedChangeListener(null);
        boolean bookmarkState = mPreferences.getBoolean(
                keyChapterBookmark + String.valueOf(chapterPosition), false);
        tbAddRemoverBookmarkChapter.setChecked(bookmarkState);

        SQLChapterContentList sqlChapterList = new SQLChapterContentList(this);
        chaptersContent = sqlChapterList.getContentList(Integer.parseInt(chapterPosition));

        LinearLayoutManager verticalList = new LinearLayoutManager(this);
        rvChapterContent.setLayoutManager(verticalList);

        chaptersContentAdapter = new ChaptersContentAdapter(chaptersContent,
                this, this, mPreferences);
        rvChapterContent.setAdapter(chaptersContentAdapter);

        tbAddRemoverBookmarkChapter.setOnCheckedChangeListener(this);
        downloadsAudio = new DownloadsAudio(this);
        showTargetTap = mPreferences.getInt("key_target_tap", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content_chapter, menu);
        itemDownloadChapter = menu.findItem(R.id.action_download_audio_chapter);
        ifChapterAudio();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_download_audio_chapter:
                if (Build.VERSION.SDK_INT >= 23) {
                    GetPermission getPermission = new GetPermission(this);
                    if (getPermission.checkedPermission()) {
                        downloadsAudio.downloadSelectively(Integer.parseInt(chapterPosition), chaptersContentAdapter);
                    } else {
                        getPermission.requestPermission();
                    }
                } else {
                    downloadsAudio.downloadSelectively(Integer.parseInt(chapterPosition), chaptersContentAdapter);
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable o, Object arg) {
        chaptersContentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tb_add_remove_bookmark_chapter:
                ContentValues valueBookmark = new ContentValues();
                valueBookmark.put("chapter_favorite", isChecked);

                if (isChecked) {
                    Toast.makeText(this, "Глава добавлена в избранное", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Глава удалена из избранного", Toast.LENGTH_SHORT).show();
                }

                mEditor.putBoolean(keyChapterBookmark + chapterPosition, isChecked).apply();

                try {
                    SQLiteDatabase database = new DBAssetHelper(this).getReadableDatabase();
                    database.update("Table_of_chapters",
                            valueBookmark,
                            "_id = ?",
                            new String[]{String.valueOf(chapterPosition)});

                    updateBookmarkChapters.setChecked(isChecked);
                    updateBookmarkChapters.notifyObservers();

                } catch (Exception e) {
                    Toast.makeText(this, "Ошибка = " + e, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateBookmarkChapters.deleteObserver(this);
        updateBookmarkItems.deleteObserver(this);
        clearMediaPlayer();
    }

    @Override
    public void eventButtons(ChaptersContentVH chaptersContentVH, final List<ChaptersContentModel> chaptersContentModel, final int position) {
        final String strNumberOfSupplications = chaptersContentModel.get(position).getIdPosition() + "/" + chaptersContentModel.size();
        final String strIdPosition = chaptersContentModel.get(position).getIdPosition();
        final String strContentArabic = chaptersContentModel.get(position).getContentArabic();
        final String strContentTranscription = chaptersContentModel.get(position).getContentTranscription();
        final String strContentRussian = chaptersContentModel.get(position).getContentRussian();

        chaptersContentVH.tbPlayPauseItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    playOnly(position);
                } else {
                    mediaPlayer.pause();
                }
                chaptersContentAdapter.isItemSelected(position);
            }
        });

        chaptersContentVH.btnCopyItemContent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                copyContent(strNumberOfSupplications, strContentArabic, strContentTranscription, strContentRussian);
            }
        });

        chaptersContentVH.btnShareItemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareContent(strNumberOfSupplications, strContentArabic, strContentTranscription, strContentRussian);
            }
        });

        chaptersContentVH.tbAddRemoveBookmarkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentValues valueBookmark = new ContentValues();
                valueBookmark.put("item_favorite", isChecked);

                if (isChecked) {
                    Toast.makeText(ChapterContentActivity.this, "Дуа добавлено в избранное", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChapterContentActivity.this, "Дуа удалено из избранного", Toast.LENGTH_SHORT).show();
                }

                mEditor.putBoolean(keyItemBookmark + strIdPosition, isChecked).apply();

                try {
                    SQLiteDatabase database = new DBAssetHelper(ChapterContentActivity.this).getReadableDatabase();
                    database.update("Table_of_dua",
                            valueBookmark,
                            "_id = ?",
                            new String[]{strIdPosition});
                    updateBookmarkItems.setChecked(isChecked);
                    updateBookmarkItems.notifyObservers();
                } catch (Exception e) {
                    Toast.makeText(ChapterContentActivity.this, "Ошибка = " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void playOnly(int position) {

        boolean checkAudioFile = new File(Environment.getExternalStorageDirectory() +
                File.separator + "FortressOfTheMuslim_audio" + File.separator + "dua" +
                chaptersContent.get(position).getIdPosition() + ".mp3").exists();

        if (checkAudioFile) {

            clearMediaPlayer();

            File audioFile = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "FortressOfTheMuslim_audio" + File.separator + "dua" +
                    chaptersContent.get(position).getIdPosition() + ".mp3");

            mediaPlayer = MediaPlayer.create(ChapterContentActivity.this, Uri.fromFile(audioFile));
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    chaptersContentAdapter.isItemSelected(-1);
                }
            });
            if (mediaPlayer != null) {
                mediaPlayer.start();
            } else {
                Toast.makeText(this, "Предоставьте приложению доступ к хранилищу", Toast.LENGTH_SHORT).show();
            }

        } else {

            Toast.makeText(this, "Файл не скачен или удалён", Toast.LENGTH_SHORT).show();

        }
    }

    private void clearMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void copyContent(String numberOfSupplication, String contentAr, String contentTr, String contentRu) {

        if (contentAr == null) {
            contentAr = "";
        }
        if (contentTr == null) {
            contentTr = "";
        }

        ClipboardManager clipboardManager = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData copyData = ClipData.newPlainText("",
                Html.fromHtml(numberOfSupplication + "<p/>" + contentAr + "<p/>" +
                        contentTr + "<p/>" + contentRu + "<p/>" + "_____________________" + "<p/>" +
                        "https://play.google.com/store/apps/details?id=jmapps.fortressofthemuslim"));
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(copyData);
            Toast.makeText(this, "Скопировано в буфер", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareContent(String numberOfSupplication, String contentAr, String contentTr, String contentRu) {
        if (contentAr == null) {
            contentAr = "";
        }
        if (contentTr == null) {
            contentTr = "";
        }

        Intent shareContent = new Intent(Intent.ACTION_SEND);
        shareContent.setType("text/*");
        shareContent.putExtra(Intent.EXTRA_TEXT,
                Html.fromHtml(numberOfSupplication + "<p/>" + contentAr + "<p/>" +
                        contentTr + "<p/>" + contentRu + "<p/>" + "_____________________" + "<p/>" +
                        "https://play.google.com/store/apps/details?id=jmapps.fortressofthemuslim").toString());
        startActivity(shareContent);
    }

    private void ifChapterAudio() {
        for (int i = 0; i < chaptersContent.size(); i++) {
            final boolean downloadItem = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "FortressOfTheMuslim_audio" + File.separator + "dua" + chaptersContent.get(i).getIdPosition() + ".mp3").exists();
            if (downloadItem) {
                itemDownloadChapter.setVisible(false);
            } else {
                itemDownloadChapter.setVisible(true);
            }
        }

        if (showTargetTap <= 5) {
            targetTap();
        }
    }

    private void targetTap() {
        if (itemDownloadChapter.isVisible()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showTargetTap++;
                    mEditor.putInt("key_target_tap", showTargetTap).apply();
                    TapTargetView.showFor(ChapterContentActivity.this,
                            TapTarget.forToolbarMenuItem(toolbar, R.id.action_download_audio_chapter,
                                    "Скачать главу", "Нажмите на иконку, чтобы скачать аудио файлы к главе")
                                    .textColor(R.color.main)
                                    .outerCircleColor(R.color.colorAccent)
                                    .cancelable(true).tintTarget(true));
                }
            }, 200);
        }
    }
}