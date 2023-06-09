package com.countries.vpn.fastsecurevpnproxy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.countries.vpn.AdsUtils.FirebaseADHandlers.AdUtils;
import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.Fragment.HomeFragment;
import com.countries.vpn.fastsecurevpnproxy.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_main);
        initListeners();
        refreshFragment(new HomeFragment());

    }

    private void initListeners() {
        binding.ivBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.showInterstitialAd(activity, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        binding.ivBrowser.setImageResource(R.drawable.ic_bottom_browser_selected);
                        binding.ivVpn.setImageResource(R.drawable.ic_bottom_vpn);
                        binding.ivSpeedtest.setImageResource(R.drawable.ic_bottom_speedtest);
                        refreshFragment(new HomeFragment());
                    }
                });


            }
        });
        binding.ivVpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.showInterstitialAd(activity, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        binding.ivVpn.setImageResource(R.drawable.ic_bottom_vpn_selected);
                        binding.ivBrowser.setImageResource(R.drawable.ic_bottom_browser);
                        binding.ivSpeedtest.setImageResource(R.drawable.ic_bottom_speedtest);
                    }
                });

            }
        });
        binding.ivSpeedtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.showInterstitialAd(activity, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        binding.ivSpeedtest.setImageResource(R.drawable.ic_bottom_speedtest_selected);
                        binding.ivBrowser.setImageResource(R.drawable.ic_bottom_browser);
                        binding.ivVpn.setImageResource(R.drawable.ic_bottom_vpn);
                    }
                });
            }
        });
    }

    private void refreshFragment(HomeFragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frm_container, fragment).commit();
    }
}