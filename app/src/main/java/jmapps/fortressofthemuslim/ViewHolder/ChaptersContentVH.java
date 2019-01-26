package jmapps.fortressofthemuslim.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import jmapps.fortressofthemuslim.Adapter.ChaptersContentAdapter;
import jmapps.fortressofthemuslim.Model.ChaptersContentModel;
import jmapps.fortressofthemuslim.R;

public class ChaptersContentVH extends RecyclerView.ViewHolder {

    public final ToggleButton tbPlayItem;
    public final SeekBar sbAudioProgress;
    public final ToggleButton tbRepeatItem;
    public final View viewIfVisible;
    public final TextView tvContentArabic;
    public final TextView tvContentTranscription;
    public final TextView tvContentRussian;
    public final TextView tvContentSource;
    public final TextView tvNumberOfSupplications;
    public final Button btnCopyItemContent;
    public final Button btnShareItemContent;
    public final ToggleButton tbAddRemoveBookmarkItem;

    public ChaptersContentVH(@NonNull View itemView) {
        super(itemView);

        tbPlayItem = itemView.findViewById(R.id.tb_play_item);
        sbAudioProgress = itemView.findViewById(R.id.sb_audio_progress);
        tbRepeatItem = itemView.findViewById(R.id.tb_repeat_item);
        viewIfVisible = itemView.findViewById(R.id.view_if_visible);
        tvContentArabic = itemView.findViewById(R.id.tv_content_arabic);
        tvContentRussian = itemView.findViewById(R.id.tv_content_russian);
        tvContentSource = itemView.findViewById(R.id.tv_content_source);
        tvContentTranscription = itemView.findViewById(R.id.tv_content_transcription);
        tvNumberOfSupplications = itemView.findViewById(R.id.tv_number_of_supplications);
        btnCopyItemContent = itemView.findViewById(R.id.btn_copy_item_content);
        btnShareItemContent = itemView.findViewById(R.id.btn_share_item_content);
        tbAddRemoveBookmarkItem = itemView.findViewById(R.id.tb_add_remove_bookmark_items);
    }

    public void bindItemButtons(ChaptersContentAdapter.SupplicationItemButtons itemButtons, ChaptersContentVH chaptersContentVH,
                                List<ChaptersContentModel> chaptersContentModel, int position) {
        itemButtons.eventButtons(chaptersContentVH, chaptersContentModel, position);
    }
}