package com.countries.vpn.AdsUtils.FirebaseADHandlers;

import static com.countries.vpn.AdsUtils.Utils.Constants.isNull;

import android.app.Application;

import com.countries.vpn.AdsUtils.PreferencesManager.AppPreferences;
import com.countries.vpn.AdsUtils.Utils.Constants;
import com.countries.vpn.AdsUtils.Utils.Global;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyApplication extends Application {
    private static MyApplication app;
    public static MyApplication getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        FirebaseApp.initializeApp(getApplicationContext());
        AppPreferences appPreferencesManger = new AppPreferences(this);
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.ADSJSON);
        Constants.adsJsonPOJO = Global.getAdsData(appPreferencesManger.getAdsModel());
        if (Constants.adsJsonPOJO != null && !isNull(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getValue())) {
            Constants.adsJsonPOJO = Global.getAdsData(appPreferencesManger.getAdsModel());
            Constants.hitCounter = Integer.parseInt(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getHits());
            MobileAds.initialize(this, initializationStatus -> {
            });
            new AppOpenAds(this);
        } else {
            FirebaseUtils.initiateAndStoreFirebaseRemoteConfig(this, adsJsonPOJO -> {
                appPreferencesManger.setAdsModel(adsJsonPOJO);
                Constants.adsJsonPOJO = adsJsonPOJO;
                Constants.hitCounter = Integer.parseInt(Constants.adsJsonPOJO.getParameters().getApp_open_ad().getDefaultValue().getHits());
                MobileAds.initialize(this, initializationStatus -> {
                });
                new AppOpenAds(this);
            });
        }

    }

}
