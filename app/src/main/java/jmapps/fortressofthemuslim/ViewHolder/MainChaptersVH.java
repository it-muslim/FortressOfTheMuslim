package jmapps.fortressofthemuslim.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import jmapps.fortressofthemuslim.Adapter.MainChaptersAdapter;
import jmapps.fortressofthemuslim.Model.MainChaptersModel;
import jmapps.fortressofthemuslim.R;

public class MainChaptersVH extends RecyclerView.ViewHolder {

    public final ToggleButton tbAddRemoveBookmark;
    public final TextView tvMainChapterName;

    public MainChaptersVH(@NonNull final View itemView) {
        super(itemView);

        tbAddRemoveBookmark = itemView.findViewById(R.id.tb_add_remove_bookmark_chapter);
        tvMainChapterName = itemView.findViewById(R.id.tv_main_chapter_name);
    }

    public void bindMainItemClick(final MainChaptersAdapter.MainChaptersItemClick mainChaptersItemClick,
                                  final MainChaptersVH mainChaptersVH, final List<MainChaptersModel> mainChaptersModel,
                                  final int position) {
        mainChaptersItemClick.mainItemClick(mainChaptersVH, mainChaptersModel, position);
    }
}