package jmapps.fortressofthemuslim.Fragment;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import jmapps.fortressofthemuslim.Adapter.MainChaptersAdapter;
import jmapps.fortressofthemuslim.ChapterContentActivity;
import jmapps.fortressofthemuslim.DBSetup.DBAssetHelper;
import jmapps.fortressofthemuslim.DBSetup.SQLChapterList;
import jmapps.fortressofthemuslim.Model.MainChaptersModel;
import jmapps.fortressofthemuslim.Observer.UpdateBookmarkChapters;
import jmapps.fortressofthemuslim.R;
import jmapps.fortressofthemuslim.ViewHolder.MainChaptersVH;

public class MainChapters extends Fragment implements Observer, MainChaptersAdapter.MainChaptersItemClick {

    public static final String keyChapterPosition = "key_chapter_content";
    public static final String keyChapterName = "key_chapter_name";
    public static final String keyChapterBookmark = "key_chapter_bookmark_";

    private SharedPreferences.Editor mEditor;
    private SearchView searchView = null;
    private MainChaptersAdapter mainChaptersAdapter;
    private UpdateBookmarkChapters updateBookmarkChapters;

    @Override
    public void onStart() {
        super.onStart();
        updateBookmarkChapters = UpdateBookmarkChapters.getInstance();
        updateBookmarkChapters.addObserver(this);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootMainChapters = inflater.inflate(R.layout.fragment_main_chapters, container, false);
        setHasOptionsMenu(true);

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mPreferences.edit();

        RecyclerView rvMainChaptersList = rootMainChapters.findViewById(R.id.rv_main_chapters_list);

        SQLChapterList sqlChapterList = new SQLChapterList(getActivity());
        List<MainChaptersModel> mainChaptersModels = sqlChapterList.getChapterList();

        LinearLayoutManager verticalList = new LinearLayoutManager(getActivity());
        rvMainChaptersList.setLayoutManager(verticalList);

        mainChaptersAdapter = new MainChaptersAdapter(mainChaptersModels, getActivity(), mPreferences,
                this);
        mainChaptersAdapter.notifyDataSetChanged();
        rvMainChaptersList.setAdapter(mainChaptersAdapter);

        return rootMainChapters;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof UpdateBookmarkChapters) {
            mainChaptersAdapter.notifyDataSetChanged();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chapters, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    if (TextUtils.isEmpty(newText)) {
                        mainChaptersAdapter.getFilter().filter("");
                    } else {
                        mainChaptersAdapter.getFilter().filter(newText);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
            };
            searchView.setOnQueryTextListener(onQueryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void mainItemClick(MainChaptersVH mainChaptersVH, List<MainChaptersModel> mainChaptersModel, final int position) {
        final String strIdPosition = mainChaptersModel.get(position).getIdPosition();
        final String strChapterName = mainChaptersModel.get(position).getChapterName();

        mainChaptersVH.tbAddRemoveBookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentValues valueBookmark = new ContentValues();
                valueBookmark.put("chapter_favorite", isChecked);

                if (isChecked) {
                    Toast.makeText(getActivity(), "Глава добавлена в избранное", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Глава удалена из избранного", Toast.LENGTH_SHORT).show();
                }

                mEditor.putBoolean(keyChapterBookmark + strIdPosition, isChecked).apply();

                try {
                    SQLiteDatabase database = new DBAssetHelper(getActivity()).getReadableDatabase();
                    database.update("Table_of_chapters",
                            valueBookmark,
                            "_id = ?",
                            new String[]{strIdPosition});

                    updateBookmarkChapters.setChecked(isChecked);
                    updateBookmarkChapters.notifyObservers();

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Ошибка = " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mainChaptersVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChapterContent = new Intent(getActivity(), ChapterContentActivity.class);
                toChapterContent.putExtra(keyChapterPosition, strIdPosition);
                toChapterContent.putExtra(keyChapterName, strChapterName);
                toChapterContent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toChapterContent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        updateBookmarkChapters.deleteObserver(this);
    }
}