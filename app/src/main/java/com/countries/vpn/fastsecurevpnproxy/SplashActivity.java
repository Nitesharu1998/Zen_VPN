package com.countries.vpn.fastsecurevpnproxy;

import static com.countries.vpn.AdsUtils.Utils.Constants.isNull;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.countries.vpn.AdsUtils.FirebaseADHandlers.AdUtils;
import com.countries.vpn.AdsUtils.FirebaseADHandlers.AdsJsonPOJO;
import com.countries.vpn.AdsUtils.FirebaseADHandlers.FirebaseUtils;
import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.AdsUtils.PreferencesManager.AppPreferences;
import com.countries.vpn.AdsUtils.Utils.Constants;
import com.countries.vpn.AdsUtils.Utils.Global;
import com.countries.vpn.fastsecurevpnproxy.databinding.ActivitySplashBinding;
import com.wireguard.android.backend.Backend;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    AppPreferences appPreferencesManger;
    Activity activity;
    ActivitySplashBinding binding;
    public static Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = SplashActivity.this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_splash);

        appPreferencesManger = new AppPreferences(this);

        if (Constants.adsJsonPOJO != null && !isNull(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getValue())) {
            Constants.adsJsonPOJO = Global.getAdsData(appPreferencesManger.getAdsModel());
            Constants.adsJsonPOJO.getParameters().getShowAd().getDefaultValue().setValue("false");
            Constants.hitCounter = Integer.parseInt(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getHits());
            proceed();
        } else {
            FirebaseUtils.initiateAndStoreFirebaseRemoteConfig(activity, new AppInterfaces.AdDataInterface() {
                @Override
                public void getAdData(AdsJsonPOJO adsJsonPOJO) {
                    //Need to call this only once per
                    appPreferencesManger.setAdsModel(adsJsonPOJO);
                    Constants.adsJsonPOJO = adsJsonPOJO;
                    Constants.hitCounter = Integer.parseInt(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getHits());
                    Constants.adsJsonPOJO.getParameters().getShowAd().getDefaultValue().setValue("false");
                    proceed();
                }
            });
        }
    }

    private void proceed() {
        AdUtils.showAdIfAvailable(activity, new AppInterfaces.AppOpenADInterface() {
            @Override
            public void appOpenAdState(boolean state_load) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (appPreferencesManger.getIsFirstRun()) {
                            startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                }, 1000);
            }
        });

    }
}