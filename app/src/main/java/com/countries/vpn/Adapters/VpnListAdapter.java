package com.countries.vpn.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.AdsUtils.Utils.Global;
import com.countries.vpn.Vpn.CountryWiseVPNModels;
import com.countries.vpn.fastsecurevpnproxy.R;
import com.countries.vpn.fastsecurevpnproxy.databinding.VpnItemBinding;

import java.util.List;

public class VpnListAdapter extends RecyclerView.Adapter<VpnListAdapter.ViewHolder> {
    Activity activity;
    AppInterfaces.AdapterClick adapterClick;
    List<CountryWiseVPNModels> countryWiseVpnList;

    public VpnListAdapter(Activity activity, List<CountryWiseVPNModels> countryWiseVpnList, AppInterfaces.AdapterClick adapterClick) {
        this.activity = activity;
        this.adapterClick = adapterClick;
        this.countryWiseVpnList = countryWiseVpnList;
    }

    @NonNull
    @Override
    public VpnListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.vpn_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VpnListAdapter.ViewHolder holder, int position) {
        holder.binding.tvCountryName.setText(countryWiseVpnList.get(position).getCountryName());
        Glide.with(activity).load(Global.getFlagOfCountry(countryWiseVpnList.get(position).getCountryName())).into(holder.binding.imgVpnFlag);
        holder.binding.tvFastestServer.setText(""+countryWiseVpnList.get(position).getModelsList().size() +"Locations");

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterClick.clickedPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryWiseVpnList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        VpnItemBinding binding;

        public ViewHolder(@NonNull VpnItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
