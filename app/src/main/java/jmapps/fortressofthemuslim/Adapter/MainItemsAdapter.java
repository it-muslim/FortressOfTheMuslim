package jmapps.fortressofthemuslim.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.File;
import java.util.List;

import jmapps.fortressofthemuslim.Model.MainItemsModel;
import jmapps.fortressofthemuslim.R;
import jmapps.fortressofthemuslim.ViewHolder.MainItemsVH;

import static jmapps.fortressofthemuslim.Fragment.MainItemsAll.keyItemBookmark;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyRuVisibleState;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyTrVisibleState;

public class MainItemsAdapter extends RecyclerView.Adapter<MainItemsVH> {

    private final List<MainItemsModel> mMainItemsModel;
    private final Context mContext;
    private final LayoutInflater inflater;
    private final SupplicationItemButtons mSupplicationItemButtons;
    private final SharedPreferences mPreferences;
    private int lastPosition = -1;
    private int currentIndex = -1;

    public interface SupplicationItemButtons {
        void eventButtons(MainItemsVH mainItemsVH, List<MainItemsModel> mainItemsModel, int position);
    }

    public MainItemsAdapter(List<MainItemsModel> mainItemsModel,
                            Context context,
                            SupplicationItemButtons supplicationItemButtons,
                            SharedPreferences preferences) {
        this.mMainItemsModel = mainItemsModel;
        this.mContext = context;
        this.mSupplicationItemButtons = supplicationItemButtons;
        this.mPreferences = preferences;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MainItemsVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootItems = inflater.inflate(R.layout.item_main_item_all, viewGroup, false);
        return new MainItemsVH(rootItems);
    }

    @Override
    public void onBindViewHolder(@NonNull MainItemsVH mainItemsVH, @SuppressLint("RecyclerView") int position) {
        String strIdPosition = mMainItemsModel.get(position).getIdPosition();
        String strContentArabic = mMainItemsModel.get(position).getContentArabic();
        String strContentTranscription = mMainItemsModel.get(position).getContentTranscription();
        String strContentRussian = mMainItemsModel.get(position).getContentRussian();
        String strContentSource = mMainItemsModel.get(position).getContentSource();

        if (strContentArabic != null) {
            mainItemsVH.tvContentArabic.setVisibility(View.VISIBLE);
            mainItemsVH.tvContentArabic.setText(Html.fromHtml(strContentArabic));
        } else {
            mainItemsVH.tvContentArabic.setVisibility(View.GONE);
        }

        boolean textTrState = mPreferences.getBoolean(keyTrVisibleState, false);
        boolean textRuState = mPreferences.getBoolean(keyRuVisibleState, false);

        if (!textTrState) {
            if (strContentTranscription != null) {
                mainItemsVH.tvContentTranscription.setVisibility(View.VISIBLE);
                mainItemsVH.tvContentTranscription.setText(Html.fromHtml(strContentTranscription));
            } else {
                mainItemsVH.tvContentTranscription.setVisibility(View.GONE);
            }
        } else {
            mainItemsVH.tvContentTranscription.setVisibility(View.GONE);
        }

        if (!textRuState) {
            mainItemsVH.tvContentRussian.setVisibility(View.VISIBLE);
            mainItemsVH.tvContentRussian.setText(Html.fromHtml(strContentRussian));
        } else {
            mainItemsVH.tvContentRussian.setVisibility(View.GONE);
        }

        mainItemsVH.tvContentRussian.setText(Html.fromHtml(strContentRussian));

        if (strContentSource != null) {
            mainItemsVH.tvContentSource.setVisibility(View.VISIBLE);
            mainItemsVH.tvContentSource.setText(strContentSource);
        } else {
            mainItemsVH.tvContentSource.setVisibility(View.GONE);
        }

        mainItemsVH.lineMain.setVisibility((currentIndex == position) ? View.INVISIBLE : View.VISIBLE);
        mainItemsVH.lineAccent.setVisibility((currentIndex == position) ? View.VISIBLE : View.INVISIBLE);

        boolean checkAudioFile = new File(Environment.getExternalStorageDirectory() +
                File.separator + "FortressOfTheMuslim_audio" + File.separator + "dua" +
                mMainItemsModel.get(position).getIdPosition() + ".mp3").exists();

//        if (checkAudioFile) {
//            mainItemsVH.tbPlayPauseItem.setVisibility(View.VISIBLE);
//        } else {
//            mainItemsVH.tbPlayPauseItem.setVisibility(View.GONE);
//        }


        if (currentIndex != position) {
            mainItemsVH.tbPlayPauseItem.setOnCheckedChangeListener(null);
            mainItemsVH.tbPlayPauseItem.setChecked(false);
        }

        String strNumberOfSupplications = position + 1 + "/" + mMainItemsModel.size();
        mainItemsVH.tvNumberOfSupplications.setText(strNumberOfSupplications);

        mainItemsVH.tbAddRemoveBookmarkItem.setOnCheckedChangeListener(null);
        boolean bookmarkState = mPreferences.getBoolean(
                keyItemBookmark + String.valueOf(strIdPosition), false);
        mainItemsVH.tbAddRemoveBookmarkItem.setChecked(bookmarkState);

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.up_from_bottom);
            mainItemsVH.itemView.startAnimation(animation);
        }
        lastPosition = position;
        mainItemsVH.bindItemButtons(mSupplicationItemButtons, mainItemsVH, mMainItemsModel, position);
    }

    @Override
    public int getItemCount() {
        return mMainItemsModel.size();
    }

    public void isItemSelected(int currentIndex) {
        this.currentIndex = currentIndex;
        notifyDataSetChanged();
    }
}