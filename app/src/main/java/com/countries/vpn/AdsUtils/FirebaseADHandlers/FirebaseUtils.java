package com.countries.vpn.AdsUtils.FirebaseADHandlers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.AdsUtils.PreferencesManager.AppPreferences;
import com.countries.vpn.AdsUtils.Utils.Global;
import com.countries.vpn.fastsecurevpnproxy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;


public class FirebaseUtils {
    static String jsonData = "";

    public static void initiateAndStoreFirebaseRemoteConfig(Context context, final AppInterfaces.AdDataInterface adDataInterface) {
        FirebaseApp.initializeApp(context);
        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(1).build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);


        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {

            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    jsonData = mFirebaseRemoteConfig.getAll().get("VpnAdsConfig").asString();
                    adDataInterface.getAdData(Global.getAdsData(jsonData));
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Global.sout("ad failed", e.getLocalizedMessage());
            }
        });
    }

    public static void getFlagsFromFirebase (Context context, AppInterfaces.FlagsInterface flagInterface){
        FirebaseApp.initializeApp(context);
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(1).build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {

            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    jsonData = mFirebaseRemoteConfig.getAll().get("VpnFlagConfig").asString();
                    flagInterface.setFlagsData(Global.setUpFlagsModel(jsonData));
                    AppPreferences manger =new AppPreferences(context);
                    manger.setFlagsModel(jsonData);
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Global.sout("flags failed", e.getLocalizedMessage());
            }
        });
    }
}
