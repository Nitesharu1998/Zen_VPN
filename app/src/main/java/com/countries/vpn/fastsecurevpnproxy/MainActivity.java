package com.countries.vpn.fastsecurevpnproxy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.countries.vpn.AdsUtils.FirebaseADHandlers.AdUtils;
import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.AdsUtils.Utils.Constants;
import com.countries.vpn.Fragment.HomeFragment;
import com.countries.vpn.Fragment.SpeedTestFragment;
import com.countries.vpn.Fragment.WebSurfingFragment;
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
        if (Constants.isPrivateTab) {
            refreshFragment(new WebSurfingFragment());
        } else refreshFragment(new HomeFragment());

    }

    public void launchBrowser(){
        binding.ivBrowser.setImageResource(R.drawable.ic_bottom_browser_selected);
        binding.ivVpn.setImageResource(R.drawable.ic_bottom_vpn);
        binding.ivSpeedtest.setImageResource(R.drawable.ic_bottom_speedtest);
        refreshFragment(new WebSurfingFragment());
    }

    private void initListeners() {
        binding.ivBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* if (Constants.isConnected) {*/
                AdUtils.showInterstitialAd(activity, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        launchBrowser();
                    }
                });
            }
        });
        binding.ivVpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* if (Constants.isConnected) {*/
                AdUtils.showInterstitialAd(activity, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        binding.ivVpn.setImageResource(R.drawable.ic_bottom_vpn_selected);
                        binding.ivBrowser.setImageResource(R.drawable.ic_bottom_browser);
                        binding.ivSpeedtest.setImageResource(R.drawable.ic_bottom_speedtest);
                        refreshFragment(new HomeFragment());
                    }
                });
//                } else {
//                    Toast.makeText(activity, "Please connect to the VPN service", Toast.LENGTH_SHORT).show();
//                }


            }
        });
        binding.ivSpeedtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (Constants.isConnected) {*/
                AdUtils.showInterstitialAd(activity, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        binding.ivSpeedtest.setImageResource(R.drawable.ic_bottom_speedtest_selected);
                        binding.ivBrowser.setImageResource(R.drawable.ic_bottom_browser);
                        binding.ivVpn.setImageResource(R.drawable.ic_bottom_vpn);
                        refreshFragment(new SpeedTestFragment());
                    }
                });
                /*} else {
                    Toast.makeText(activity, "Please connect to the VPN service", Toast.LENGTH_SHORT).show();
                }*/

            }
        });
    }

    public void refreshFragment(Fragment fragment) {
        binding.frmContainer.removeAllViews();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frm_container, fragment).commit();
    }
}