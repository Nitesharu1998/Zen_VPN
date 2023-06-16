package com.countries.vpn.fastsecurevpnproxy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.countries.vpn.AdsUtils.FirebaseADHandlers.AdUtils;
import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.AdsUtils.Utils.Constants;
import com.countries.vpn.AdsUtils.Utils.Global;
import com.countries.vpn.Vpn.APIHandler.VPNApiCallHandler;
import com.countries.vpn.Vpn.TunnelModel;
import com.countries.vpn.fastsecurevpnproxy.databinding.ActivityIntroBinding;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {
    ActivityIntroBinding binding;
    Activity activity;
    int position = 0;
    String[] introHeadings = {"Privacy Matters", "Embrace Safer Surfing"};
    String[] introSubHeadings = {"You’ve unlocked the secret gateway to a safer web. Embrace the power of our VPN Browser app and explore fearlessly!", "Say goodbye to digital restrictions and hello to a thrilling journey where privacy meets adventure. Buckle up, we’re just getting Started! "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_intro);



        handleNext(position);

        binding.ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.showInterstitialAd(activity, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        position++;
                        handleNext(position);
                    }
                });

            }
        });
        binding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.showInterstitialAd(activity, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        handleNext(2);
                    }
                });
            }
        });
    }

    private void handleNext(int position) {
        binding.introlottie.setImageAssetsFolder("raw");
        switch (position) {
            case 0:
                binding.tvHeading.setText(introHeadings[position]);
                binding.tvSubheading.setText(introSubHeadings[position]);
                break;
            case 1:
                binding.ivProgress.setRotation(180);
                binding.introlottie.setVisibility(View.GONE);
                binding.introlottie2.setVisibility(View.VISIBLE);
                binding.tvHeading.setText(introHeadings[position]);
                binding.tvSubheading.setText(introSubHeadings[position]);
                break;
            case 2:
                startActivity(new Intent(activity, MainActivity.class));
                finish();
                break;
        }

    }
}