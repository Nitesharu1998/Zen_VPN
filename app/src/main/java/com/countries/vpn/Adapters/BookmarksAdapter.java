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
import com.countries.vpn.fastsecurevpnproxy.databinding.SavedBookmarksBinding;
import com.countries.vpn.fastsecurevpnproxy.databinding.SavedBookmarksBinding;

import java.util.ArrayList;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {
    Context context;
    ArrayList<WebLinksDataModel> dataModels;
    AppInterfaces.AdapterClick adapterClick;

    public BookmarksAdapter(Context context, ArrayList<WebLinksDataModel> dataModels, AppInterfaces.AdapterClick adapterClick) {
        this.context = context;
        this.dataModels = dataModels;
        this.adapterClick = adapterClick;
    }

    @NonNull
    @Override
    public BookmarksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SavedBookmarksBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.saved_bookmarks, parent, false);
        return new BookmarksAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarksAdapter.ViewHolder holder, int position) {
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
        SavedBookmarksBinding binding;

        public ViewHolder(@NonNull SavedBookmarksBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
