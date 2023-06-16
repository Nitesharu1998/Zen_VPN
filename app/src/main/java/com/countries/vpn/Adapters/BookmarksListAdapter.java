package com.countries.vpn.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.CommonDataModels.WebLinksDataModel;
import com.countries.vpn.fastsecurevpnproxy.R;
import com.countries.vpn.fastsecurevpnproxy.databinding.BookmarksItemBinding;

import java.util.ArrayList;

public class BookmarksListAdapter extends RecyclerView.Adapter<BookmarksListAdapter.ViewHolder> {
    Context context;
    ArrayList<WebLinksDataModel> dataModels;
    AppInterfaces.AdapterClick adapterClick;

    public BookmarksListAdapter(Context context, ArrayList<WebLinksDataModel> dataModels, AppInterfaces.AdapterClick adapterClick) {
        this.context = context;
        this.dataModels = dataModels;
        this.adapterClick = adapterClick;
    }

    @NonNull
    @Override
    public BookmarksListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookmarksItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_bookmarks, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarksListAdapter.ViewHolder holder, int position) {
        holder.binding.tvBookmarkLink.setText(dataModels.get(position).getURL());
        holder.binding.tvBookmarkTitle.setText(dataModels.get(position).getName());
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterClick.clickedPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        BookmarksItemBinding binding;

        public ViewHolder(@NonNull BookmarksItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
