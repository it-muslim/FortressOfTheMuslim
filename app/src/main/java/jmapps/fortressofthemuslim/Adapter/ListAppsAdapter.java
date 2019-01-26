package jmapps.fortressofthemuslim.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jmapps.fortressofthemuslim.Model.ListAppM;
import jmapps.fortressofthemuslim.R;
import jmapps.fortressofthemuslim.ViewHolder.ListAppViewHolder;

public class ListAppsAdapter extends RecyclerView.Adapter<ListAppViewHolder> {

    private final List<ListAppM> mListAppM;

    public ListAppsAdapter(List<ListAppM> listAppM) {
        this.mListAppM = listAppM;
    }

    @NonNull
    @Override
    public ListAppViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int parent) {
        View listAppRoot = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_list_apps, viewGroup, false);
        return new ListAppViewHolder(listAppRoot);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListAppViewHolder holder, final int position) {
        int iconApp = mListAppM.get(position).getIconApp();
        String strNameApp = mListAppM.get(position).getStrNameApp();
        final String strLinkApp = mListAppM.get(position).getStrLinkApp();

        holder.ivIconApp.setBackgroundResource(iconApp);
        holder.tvNameApp.setText(strNameApp);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPlayMarket = new Intent(Intent.ACTION_VIEW);
                toPlayMarket.setData(Uri.parse(strLinkApp));
                holder.itemView.getContext().startActivity(toPlayMarket);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListAppM.size();
    }
}