package com.countries.vpn.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.fastsecurevpnproxy.R;
import com.countries.vpn.fastsecurevpnproxy.databinding.TabLayoutItemBinding;

import java.util.ArrayList;

public class WebTabsAdapter extends RecyclerView.Adapter<WebTabsAdapter.ViewHolder> {
    ArrayList<String> tabLinksList;
    Activity activity;
    AppInterfaces.TabAdapterInterface tabAdapterInterface;

    public WebTabsAdapter(ArrayList<String> tabLinksList, Activity activity, AppInterfaces.TabAdapterInterface tabAdapterInterface) {
        this.tabLinksList = tabLinksList;
        this.activity = activity;
        this.tabAdapterInterface = tabAdapterInterface;
    }

    @NonNull
    @Override
    public WebTabsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TabLayoutItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.tab_layout_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WebTabsAdapter.ViewHolder holder, int position) {

        if (tabLinksList.get(position).isEmpty()) {
            holder.binding.ivAddNewTab.setVisibility(View.VISIBLE);
            holder.binding.rlWebview.setVisibility(View.GONE);
        } else {
            holder.binding.ivAddNewTab.setVisibility(View.GONE);
            holder.binding.rlWebview.setVisibility(View.VISIBLE);
        }

        holder.binding.wvLoadLink.loadUrl(tabLinksList.get(position));
        holder.binding.wvLoadLink.setWebViewClient(new WebViewClient());
        holder.binding.wvLoadLink.getSettings().setJavaScriptEnabled(true);
        holder.binding.wvLoadLink.setWebViewClient(new WebViewClient());
        holder.binding.wvLoadLink.getSettings().setLoadWithOverviewMode(true);
        holder.binding.wvLoadLink.getSettings().setUseWideViewPort(true);


        holder.binding.wvLoadLink.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                tabAdapterInterface.hitLink(position);
                return true;
            }
        });

        holder.binding.ivAddNewTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabAdapterInterface.addNewTab();
            }
        });

        holder.binding.rlWebview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabAdapterInterface.hitLink(position);
            }
        });
        holder.binding.ivCloseTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabAdapterInterface.removeTab(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tabLinksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TabLayoutItemBinding binding;

        public ViewHolder(@NonNull TabLayoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
