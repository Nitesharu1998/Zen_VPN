package com.countries.vpn.AdsUtils.FirebaseADHandlers;

import static com.countries.vpn.AdsUtils.FirebaseADHandlers.MyApplication.getPreferences;
import static java.util.Objects.isNull;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.countries.vpn.AdsUtils.PreferencesManager.AppPreferences;
import com.countries.vpn.AdsUtils.Utils.Constants;
import com.countries.vpn.Vpn.VPNInitiatorHandler;
import com.countries.vpn.Vpn.VpnInterfaces;
import com.countries.vpn.fastsecurevpnproxy.SplashActivity;

public class AppOpenAds implements LifecycleObserver, Application.ActivityLifecycleCallbacks {

    private Activity currentActivity;
    MyApplication application;
    boolean isAdShowing;


    public AppOpenAds(MyApplication application) {
        this.application = application;
        application.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        isAdShowing = false;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        if (!isAdShowing && currentActivity != null && (!currentActivity.getClass().getName().equals(SplashActivity.class.getName()))) {
            isAdShowing = true;
            AdUtils.showAdIfAvailable(currentActivity, state_load -> {
                isAdShowing = false;
            });
        } else {
            isAdShowing = false;
        }


    }


    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
        AdUtils.loadInitialInterstitialAds(activity);
        AdUtils.loadAppOpenAds(activity);
        AdUtils.loadInitialNativeList(activity);
        if (!isNull(Constants.randomTunnelModel.getCountry()) && Constants.isConnected) {
            VPNInitiatorHandler.connectVPN(activity, Constants.randomTunnelModel, new VpnInterfaces.vpnConnectionInterface() {
                @Override
                public void isConnected(boolean state, String ipAddress) {
                    Constants.isConnected = state;
                }
            });
        }

    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
        VPNInitiatorHandler.disconnectVPN();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }


}
