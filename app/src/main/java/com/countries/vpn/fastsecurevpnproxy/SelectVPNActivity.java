package com.countries.vpn.fastsecurevpnproxy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.countries.vpn.Adapters.VpnListAdapter;
import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.AdsUtils.Utils.Constants;
import com.countries.vpn.fastsecurevpnproxy.databinding.ActivitySelectVpnactivityBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.Random;

public class SelectVPNActivity extends AppCompatActivity {
    ActivitySelectVpnactivityBinding binding;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_select_vpnactivity);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setTabs();

    }

    private void setTabs() {
        binding.tabSelector.addTab(binding.tabSelector.newTab().setText("Free"));
        binding.tabSelector.addTab(binding.tabSelector.newTab().setText("VIP"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(RecyclerView.VERTICAL);
        binding.rclVpnList.setLayoutManager(manager);
        VpnListAdapter adapter = new VpnListAdapter(activity, Constants.countryWiseVpnList, new AppInterfaces.AdapterClick() {
            @Override
            public void clickedPosition(int position) {
                Constants.randomTunnelModel = Constants.countryWiseVpnList.get(position).getModelsList().get(new Random().nextInt(Constants.countryWiseVpnList.get(position).getModelsList().size()));
                onBackPressed();
            }
        });
        binding.rclVpnList.setAdapter(adapter);


        binding.tabSelector.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setVPNList(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setVPNList(int position) {
        if (position == 0) {

        } else {
        }
    }
}