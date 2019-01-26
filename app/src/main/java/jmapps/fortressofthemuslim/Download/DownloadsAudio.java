package jmapps.fortressofthemuslim.Download;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jmapps.fortressofthemuslim.R;

public class DownloadsAudio {

    private Context mContext;
    private ProgressDialog progressDownload;
    private List<LinkAudioModel> modelList;

    public DownloadsAudio(Context context) {
        this.mContext = context;
    }

    public void downloadAllAudios() {
        SQLAudioLinksList sqlAudioLinksList = new SQLAudioLinksList(mContext);
        modelList = sqlAudioLinksList.getAllLinks();

        progressDownload = new ProgressDialog(mContext);
        progressDownload.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDownload.setIcon(R.drawable.ic_download);
        progressDownload.setTitle("Скачивание начинается...");
        progressDownload.setMessage("Пожалуйста, дождитесь пока скачаются все аудио файлы...");
        progressDownload.setMax(modelList.size());
        progressDownload.setCancelable(false);
        progressDownload.show();

        FileDownloadListener downloadListener = allListener();
        FileDownloader.getImpl().bindService();
        FileDownloader.getImpl().clearAllTaskData();

        File pathToMainFolder = new File(Environment.getExternalStorageDirectory(),
                File.separator + "FortressOfTheMuslim_audio" + File.separator + "dua");

        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(downloadListener);
        final List<BaseDownloadTask> tasks = new ArrayList<>();

        for (int i = 0; i < modelList.size(); i++) {
            tasks.add(FileDownloader.getImpl().create(modelList.get(i).getLinkAudio())
                    .setPath(pathToMainFolder + modelList.get(i).getIdPosition() + ".mp3"));
        }

        queueSet.disableCallbackProgressTimes();
        queueSet.setAutoRetryTimes(1);
        queueSet.downloadTogether(tasks);
        queueSet.start();
    }

    private FileDownloadListener allListener() {
        return new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                progressDownload.setProgress(progressDownload.getProgress() + 1);
                progressDownload.setTitle("Скачивание...");
                if (progressDownload.getProgress() == modelList.size()) {
                    Toast.makeText(mContext, "Скачивание файлов завершено", Toast.LENGTH_SHORT).show();
                    progressDownload.dismiss();
                }
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                Toast.makeText(mContext, "Скачивание приостановленно", Toast.LENGTH_SHORT).show();
                progressDownload.dismiss();
                FileDownloader.getImpl().pauseAll();
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                progressDownload.dismiss();
                Toast.makeText(mContext, "Ошибка скачивания", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        };
    }

    public void downloadSelectively(int idPosition, RecyclerView.Adapter adapter) {

        SQLAudioLinksList sqlAudioLinksList = new SQLAudioLinksList(mContext);
        modelList = sqlAudioLinksList.getSelectivelyLinks(idPosition);

        progressDownload = new ProgressDialog(mContext);
        progressDownload.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDownload.setIcon(R.drawable.ic_download);
        progressDownload.setTitle("Скачивание начинается...");
        progressDownload.setMessage("Пожалуйста, дождитесь пока скачаются аудио файлы главы...");
        progressDownload.setMax(modelList.size());
        progressDownload.setCancelable(false);
        progressDownload.show();

        FileDownloadListener downloadListener = createListener(adapter);
        FileDownloader.getImpl().bindService();
        FileDownloader.getImpl().clearAllTaskData();

        File mediaFolderStorageDir = new File(Environment.getExternalStorageDirectory(),
                "FortressOfTheMuslim_audio" + File.separator + "dua");

        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(downloadListener);
        final List<BaseDownloadTask> tasks = new ArrayList<>();

        for (int i = 0; i < modelList.size(); i++) {
            tasks.add(FileDownloader.getImpl().create(modelList.get(i).getLinkAudio())
                    .setPath(mediaFolderStorageDir + modelList.get(i).getIdPosition() + ".mp3"));
        }

        queueSet.disableCallbackProgressTimes();
        queueSet.setAutoRetryTimes(1);
        queueSet.downloadTogether(tasks);
        queueSet.start();
    }

    private FileDownloadListener createListener(final RecyclerView.Adapter adapter) {
        return new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                progressDownload.setProgress(progressDownload.getProgress() + 1);
                progressDownload.setTitle("Скачивание...");
                if (progressDownload.getProgress() == modelList.size()) {
                    adapter.notifyDataSetChanged();
                    Toast.makeText(mContext, "Скачивание главы завершено", Toast.LENGTH_SHORT).show();
                    progressDownload.dismiss();
                }
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                Toast.makeText(mContext, "Скачивание приостановленно", Toast.LENGTH_SHORT).show();
                progressDownload.dismiss();
                FileDownloader.getImpl().pauseAll();
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                progressDownload.dismiss();
                Toast.makeText(mContext, "Ошибка скачивания", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        };
    }
}