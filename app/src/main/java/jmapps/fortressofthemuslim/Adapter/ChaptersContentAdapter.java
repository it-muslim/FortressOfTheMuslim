package jmapps.fortressofthemuslim.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;

import jmapps.fortressofthemuslim.Model.ChaptersContentModel;
import jmapps.fortressofthemuslim.R;
import jmapps.fortressofthemuslim.ViewHolder.ChaptersContentVH;

import static jmapps.fortressofthemuslim.Fragment.MainItemsAll.keyItemBookmark;

public class ChaptersContentAdapter extends RecyclerView.Adapter<ChaptersContentVH> {

    private final List<ChaptersContentModel> mChaptersContentsModel;
    private final Context mContext;
    private final LayoutInflater inflater;
    private final SupplicationItemButtons mSupplicationItemButtons;
    private final SharedPreferences mPreferences;
    private int currentIndex = -1;

    public interface SupplicationItemButtons {
        void eventButtons(ChaptersContentVH chaptersContentVH, List<ChaptersContentModel> chaptersContentModel, int position);
    }

    public ChaptersContentAdapter(List<ChaptersContentModel> chaptersContentModel,
                                  Context context,
                                  SupplicationItemButtons supplicationItemButtons,
                                  SharedPreferences preferences) {
        this.mChaptersContentsModel = chaptersContentModel;
        this.mContext = context;
        this.mSupplicationItemButtons = supplicationItemButtons;
        this.mPreferences = preferences;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ChaptersContentVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int parent) {
        View rootContents = inflater.inflate(R.layout.item_main_chapter_content, viewGroup, false);
        return new ChaptersContentVH(rootContents);
    }

    @Override
    public void onBindViewHolder(@NonNull ChaptersContentVH chaptersContentVH, int position) {
        String strIdPosition = mChaptersContentsModel.get(position).getIdPosition();
        String strContentArabic = mChaptersContentsModel.get(position).getContentArabic();
        String strContentTranscription = mChaptersContentsModel.get(position).getContentTranscription();
        String strContentRussian = mChaptersContentsModel.get(position).getContentRussian();
        String strContentSource = mChaptersContentsModel.get(position).getContentSource();

        if (strContentArabic != null) {
            chaptersContentVH.tvContentArabic.setVisibility(View.VISIBLE);
            chaptersContentVH.tvContentArabic.setText(Html.fromHtml(strContentArabic));
        } else {
            chaptersContentVH.tvContentArabic.setVisibility(View.GONE);
        }

        if (!chaptersContentVH.textTrState) {
            if (strContentTranscription != null) {
                chaptersContentVH.tvContentTranscription.setVisibility(View.VISIBLE);
                chaptersContentVH.tvContentTranscription.setText(Html.fromHtml(strContentTranscription));
            } else {
                chaptersContentVH.tvContentTranscription.setVisibility(View.GONE);
            }
        } else {
            chaptersContentVH.tvContentTranscription.setVisibility(View.GONE);
        }

        if (!chaptersContentVH.textRuState) {
            chaptersContentVH.tvContentRussian.setVisibility(View.VISIBLE);
            chaptersContentVH.tvContentRussian.setText(Html.fromHtml(strContentRussian));
        } else {
            chaptersContentVH.tvContentRussian.setVisibility(View.GONE);
        }

        if (strContentSource != null) {
            chaptersContentVH.tvContentSource.setVisibility(View.VISIBLE);
            chaptersContentVH.tvContentSource.setText(strContentSource);
        } else {
            chaptersContentVH.tvContentSource.setVisibility(View.GONE);
        }

        String strNumberOfSupplications = position + 1 + "/" + mChaptersContentsModel.size();
        chaptersContentVH.tvNumberOfSupplications.setText(strNumberOfSupplications);

        chaptersContentVH.tbAddRemoveBookmarkItem.setOnCheckedChangeListener(null);
        boolean bookmarkState = mPreferences.getBoolean(
                keyItemBookmark + String.valueOf(strIdPosition), false);
        chaptersContentVH.tbAddRemoveBookmarkItem.setChecked(bookmarkState);

        chaptersContentVH.lineMain.setVisibility((currentIndex == position) ? View.INVISIBLE : View.VISIBLE);
        chaptersContentVH.lineAccent.setVisibility((currentIndex == position) ? View.VISIBLE : View.INVISIBLE);

        boolean checkAudioFile = new File(Environment.getExternalStorageDirectory() +
                File.separator + "FortressOfTheMuslim_audio" + File.separator + "dua" +
                mChaptersContentsModel.get(position).getIdPosition() + ".mp3").exists();

        if (checkAudioFile) {
            chaptersContentVH.tbPlayPauseItem.setVisibility(View.VISIBLE);
        } else {
            chaptersContentVH.tbPlayPauseItem.setVisibility(View.GONE);
        }

        if (currentIndex != position) {
            chaptersContentVH.tbPlayPauseItem.setOnCheckedChangeListener(null);
            chaptersContentVH.tbPlayPauseItem.setChecked(false);
        }

        chaptersContentVH.bindItemButtons(mSupplicationItemButtons, chaptersContentVH, mChaptersContentsModel, position);
    }

    @Override
    public int getItemCount() {
        return mChaptersContentsModel.size();
    }

    public void isItemSelected(int currentIndex) {
        this.currentIndex = currentIndex;
        notifyDataSetChanged();
    }
}