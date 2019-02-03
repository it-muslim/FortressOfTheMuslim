package jmapps.fortressofthemuslim.ViewHolder;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import jmapps.fortressofthemuslim.Adapter.MainItemsAdapter;
import jmapps.fortressofthemuslim.Model.MainItemsModel;
import jmapps.fortressofthemuslim.R;

import static jmapps.fortressofthemuslim.Fragment.Settings.keyBStateArabic;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyBStateRussian;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyBStateTranscription;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyGStateArabic;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyGStateRussian;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyGStateTranscription;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyRStateArabic;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyRStateRussian;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyRStateTranscription;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyTextArSize;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyTextRuSize;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyTextTrSize;

public class MainItemsVH extends RecyclerView.ViewHolder implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences mPreferences;

    public final View lineMain;
    public final View lineAccent;
    public final ToggleButton tbPlayPauseItem;
    public final TextView tvContentArabic;
    public final TextView tvContentTranscription;
    public final TextView tvContentRussian;
    public final TextView tvContentSource;
    public final TextView tvNumberOfSupplications;
    public final Button btnCopyItemContent;
    public final Button btnShareItemContent;
    public final ToggleButton tbAddRemoveBookmarkItem;

    public MainItemsVH(@NonNull View itemView) {
        super(itemView);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());

        PreferenceManager.getDefaultSharedPreferences(itemView.getContext()).
                registerOnSharedPreferenceChangeListener(this);

        lineMain = itemView.findViewById(R.id.line_main);
        lineAccent = itemView.findViewById(R.id.line_accent);
        tbPlayPauseItem = itemView.findViewById(R.id.tb_play_pause_item);
        tvContentArabic = itemView.findViewById(R.id.tv_content_arabic);
        tvContentTranscription = itemView.findViewById(R.id.tv_content_transcription);
        tvContentRussian = itemView.findViewById(R.id.tv_content_russian);
        tvContentSource = itemView.findViewById(R.id.tv_content_source);
        tvNumberOfSupplications = itemView.findViewById(R.id.tv_number_of_supplications);
        btnCopyItemContent = itemView.findViewById(R.id.btn_copy_item_content);
        btnShareItemContent = itemView.findViewById(R.id.btn_share_item_content);
        tbAddRemoveBookmarkItem = itemView.findViewById(R.id.tb_add_remove_bookmark_items);

        setTextSizeItems();
        setTextColorItems();
    }

    public void bindItemButtons(MainItemsAdapter.SupplicationItemButtons itemButtons, MainItemsVH mainItemsVH,
                                List<MainItemsModel> mainItemsModel, int position) {
        itemButtons.eventButtons(mainItemsVH, mainItemsModel, position);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setTextSizeItems();
        setTextColorItems();
    }


    private void setTextSizeItems() {
        int textArabicSize = mPreferences.getInt(keyTextArSize, 18);
        int textTranscriptionSize = mPreferences.getInt(keyTextTrSize, 18);
        int textRussianSize = mPreferences.getInt(keyTextRuSize, 18);

        tvContentArabic.setTextSize(textArabicSize);
        tvContentTranscription.setTextSize(textTranscriptionSize);
        tvContentRussian.setTextSize(textRussianSize);
    }

    private void setTextColorItems() {
        int ARStateAr = mPreferences.getInt(keyRStateArabic, 0);
        int AGStateAr = mPreferences.getInt(keyGStateArabic, 0);
        int ABStateAr = mPreferences.getInt(keyBStateArabic, 0);
        tvContentArabic.setTextColor(Color.argb(255, ARStateAr, AGStateAr, ABStateAr));

        int ARStateTr = mPreferences.getInt(keyRStateTranscription, 0);
        int AGStateTr = mPreferences.getInt(keyGStateTranscription, 0);
        int ABStateTr = mPreferences.getInt(keyBStateTranscription, 0);
        tvContentTranscription.setTextColor(Color.argb(255, ARStateTr, AGStateTr, ABStateTr));

        int ARStateRu = mPreferences.getInt(keyRStateRussian, 0);
        int AGStateRu = mPreferences.getInt(keyGStateRussian, 0);
        int ABStateRu = mPreferences.getInt(keyBStateRussian, 0);
        tvContentRussian.setTextColor(Color.argb(255, ARStateRu, AGStateRu, ABStateRu));
    }
}