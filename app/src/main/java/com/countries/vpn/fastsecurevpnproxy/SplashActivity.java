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
import com.countries.vpn.AdsUtils.Utils.AppAsyncTasks;
import com.countries.vpn.AdsUtils.Utils.Constants;
import com.countries.vpn.AdsUtils.Utils.Global;
import com.countries.vpn.fastsecurevpnproxy.databinding.ActivitySplashBinding;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    AppPreferences appPreferencesManger;
    Activity activity;
    ActivitySplashBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = SplashActivity.this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_splash);
        appPreferencesManger = new AppPreferences(this);
        AppAsyncTasks.GetFlags getFlags = new AppAsyncTasks.GetFlags(activity);
        getFlags.execute();
        if (Constants.adsJsonPOJO != null && !isNull(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getValue())) {
            Constants.adsJsonPOJO = Global.getAdsData(appPreferencesManger.getAdsModel());
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


                        startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                            finish();

                    }
                }, 1000);
            }
        });
    }
}