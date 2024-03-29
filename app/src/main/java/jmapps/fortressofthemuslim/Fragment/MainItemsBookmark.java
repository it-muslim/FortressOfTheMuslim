package jmapps.fortressofthemuslim.Fragment;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import jmapps.fortressofthemuslim.Adapter.MainItemsAdapter;
import jmapps.fortressofthemuslim.DBSetup.DBAssetHelper;
import jmapps.fortressofthemuslim.DBSetup.SQLItemList;
import jmapps.fortressofthemuslim.Model.MainItemsModel;
import jmapps.fortressofthemuslim.Observer.UpdateBookmarkItems;
import jmapps.fortressofthemuslim.R;
import jmapps.fortressofthemuslim.ViewHolder.MainItemsVH;

import static jmapps.fortressofthemuslim.Fragment.MainItemsAll.keyItemBookmark;

public class MainItemsBookmark extends Fragment implements Observer,
        MainItemsAdapter.SupplicationItemButtons {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private RecyclerView rvMainBookmarksList;
    private List<MainItemsModel> mainItemsModel;
    private MainItemsAdapter mainItemsAdapter;
    private TextView tvIsBookmarkListEmpty;

    private UpdateBookmarkItems updateBookmarkItems;

    @Override
    public void onStart() {
        super.onStart();
        updateBookmarkItems = UpdateBookmarkItems.getInstance();
        updateBookmarkItems.addObserver(this);
        updateBookmarkItems.setChecked(updateBookmarkItems.isChecked());
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootMainChapters = inflater.inflate(R.layout.fragment_main_items_bookmark, container, false);
        setRetainInstance(true);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mPreferences.edit();

        rvMainBookmarksList = rootMainChapters.findViewById(R.id.rv_main_items_bookmarks_list);
        tvIsBookmarkListEmpty = rootMainChapters.findViewById(R.id.tv_is_bookmark_list_empty);

        initList();

        return rootMainChapters;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof UpdateBookmarkItems) {
            initList();
        }
    }

    @Override
    public void eventButtons(MainItemsVH mainItemsVH, List<MainItemsModel> mainItemsModel, final int position) {
        final String strIdPosition = mainItemsModel.get(position).getIdPosition();
        final String strNumberOfSupplications = mainItemsModel.get(position).getIdPosition() + "/" + mainItemsModel.size();
        final String strContentArabic = mainItemsModel.get(position).getContentArabic();
        final String strContentTranscription = mainItemsModel.get(position).getContentTranscription();
        final String strContentRussian = mainItemsModel.get(position).getContentRussian();

        mainItemsVH.tbPlayPauseItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mainItemsAdapter.isItemSelected(position);
            }
        });

        mainItemsVH.btnCopyItemContent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                copyContent(strNumberOfSupplications, strContentArabic, strContentTranscription, strContentRussian);
            }
        });

        mainItemsVH.btnShareItemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareContent(strNumberOfSupplications, strContentArabic, strContentTranscription, strContentRussian);
            }
        });

        mainItemsVH.tbAddRemoveBookmarkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentValues valueBookmark = new ContentValues();
                valueBookmark.put("item_favorite", isChecked);

                if (isChecked) {
                    Toast.makeText(getActivity(), "Дуа добавлено в избранное", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Дуа удалено из избранного", Toast.LENGTH_SHORT).show();
                }

                mEditor.putBoolean(keyItemBookmark + strIdPosition, isChecked).apply();

                try {
                    SQLiteDatabase database = new DBAssetHelper(getActivity()).getReadableDatabase();
                    database.update("Table_of_dua",
                            valueBookmark,
                            "_id = ?",
                            new String[]{strIdPosition});

                    updateBookmarkItems.setChecked(isChecked);
                    updateBookmarkItems.notifyObservers();

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Ошибка = " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        updateBookmarkItems.deleteObserver(this);
    }

    private void initList() {
        SQLItemList sqlItemList = new SQLItemList(getActivity());
        mainItemsModel = sqlItemList.getBookmarkList();

        LinearLayoutManager verticalList = new LinearLayoutManager(getActivity());
        rvMainBookmarksList.setLayoutManager(verticalList);

        mainItemsAdapter = new MainItemsAdapter(mainItemsModel, getActivity(),
                this, mPreferences);

        if (mainItemsAdapter.getItemCount() == 0) {
            tvIsBookmarkListEmpty.setVisibility(View.VISIBLE);
            rvMainBookmarksList.setVisibility(View.GONE);
        } else {
            tvIsBookmarkListEmpty.setVisibility(View.GONE);
            rvMainBookmarksList.setVisibility(View.VISIBLE);
        }

        rvMainBookmarksList.setAdapter(mainItemsAdapter);
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
                Objects.requireNonNull(getContext()).getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData copyData = ClipData.newPlainText("",
                Html.fromHtml(numberOfSupplication + "<p/>" + contentAr + "<p/>" +
                        contentTr + "<p/>" + contentRu + "<p/>" + "_____________________" + "<p/>" +
                        "https://play.google.com/store/apps/details?id=jmapps.fortressofthemuslim"));
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(copyData);
            Toast.makeText(getActivity(), "Скопировано в буфер", Toast.LENGTH_SHORT).show();
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
}