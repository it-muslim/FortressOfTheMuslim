package jmapps.fortressofthemuslim.ViewHolder;

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

public class MainItemsVH extends RecyclerView.ViewHolder {

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

        tvContentArabic = itemView.findViewById(R.id.tv_content_arabic);
        tvContentTranscription = itemView.findViewById(R.id.tv_content_transcription);
        tvContentRussian = itemView.findViewById(R.id.tv_content_russian);
        tvContentSource = itemView.findViewById(R.id.tv_content_source);
        tvNumberOfSupplications = itemView.findViewById(R.id.tv_number_of_supplications);
        btnCopyItemContent = itemView.findViewById(R.id.btn_copy_item_content);
        btnShareItemContent = itemView.findViewById(R.id.btn_share_item_content);
        tbAddRemoveBookmarkItem = itemView.findViewById(R.id.tb_add_remove_bookmark_items);
    }

    public void bindItemButtons(MainItemsAdapter.SupplicationItemButtons itemButtons, MainItemsVH mainItemsVH,
                                List<MainItemsModel> mainItemsModel, int position) {
        itemButtons.eventButtons(mainItemsVH, mainItemsModel, position);
    }
}