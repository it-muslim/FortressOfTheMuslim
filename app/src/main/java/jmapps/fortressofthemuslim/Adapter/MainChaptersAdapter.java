package jmapps.fortressofthemuslim.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import jmapps.fortressofthemuslim.Model.MainChaptersModel;
import jmapps.fortressofthemuslim.R;
import jmapps.fortressofthemuslim.ViewHolder.MainChaptersVH;

import static jmapps.fortressofthemuslim.Fragment.MainChapters.keyChapterBookmark;

public class MainChaptersAdapter extends RecyclerView.Adapter<MainChaptersVH> implements Filterable {

    private List<MainChaptersModel> firstMainChapterModel;
    private List<MainChaptersModel> mMainChaptersModel;
    private Context mContext;
    private final SharedPreferences mPreferences;
    private final LayoutInflater inflater;
    private final MainChaptersItemClick mMainChaptersItemClick;
    private int lastPosition = -1;

    public interface MainChaptersItemClick {
        void mainItemClick(MainChaptersVH mainChaptersVH, List<MainChaptersModel> mainChaptersModel, int position);
    }

    public MainChaptersAdapter(List<MainChaptersModel> mMainChaptersModel,
                               Context context,
                               SharedPreferences preferences,
                               MainChaptersItemClick mainChaptersItemClick) {
        this.mMainChaptersModel = mMainChaptersModel;
        this.mPreferences = preferences;
        this.mContext = context;
        this.mMainChaptersItemClick = mainChaptersItemClick;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MainChaptersVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int parent) {
        View rootMainChapter = inflater.inflate(R.layout.item_main_chapter, viewGroup, false);
        return new MainChaptersVH(rootMainChapter);
    }

    @Override
    public void onBindViewHolder(@NonNull MainChaptersVH mainChaptersVH, @SuppressLint("RecyclerView") int position) {
        String strNameMainChapter = mMainChaptersModel.get(position).getChapterName();
        int idPosition = Integer.valueOf(mMainChaptersModel.get(position).getIdPosition());
        mainChaptersVH.tvMainChapterName.setText(Html.fromHtml(strNameMainChapter));

        mainChaptersVH.tbAddRemoveBookmark.setOnCheckedChangeListener(null);
        boolean bookmarkState = mPreferences.getBoolean(
                keyChapterBookmark + String.valueOf(idPosition), false);
        mainChaptersVH.tbAddRemoveBookmark.setChecked(bookmarkState);

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.up_from_bottom);
            mainChaptersVH.itemView.startAnimation(animation);
        }
        lastPosition = position;

        mainChaptersVH.bindMainItemClick(mMainChaptersItemClick, mainChaptersVH, mMainChaptersModel, position);
    }

    @Override
    public int getItemCount() {
        return mMainChaptersModel.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                final FilterResults filterResults = new FilterResults();
                final List<MainChaptersModel> results = new ArrayList<>();
                if (firstMainChapterModel == null) {
                    firstMainChapterModel = mMainChaptersModel;
                }

                if (constraint != null) {
                    if (firstMainChapterModel != null & (
                            firstMainChapterModel != null ? firstMainChapterModel.size() : 0) > 0) {
                        for (final MainChaptersModel mainChaptersModel : firstMainChapterModel) {
                            if (mainChaptersModel.getChapterName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                                results.add(mainChaptersModel);
                            }
                        }
                    }
                    filterResults.values = results;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mMainChaptersModel = (List<MainChaptersModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}