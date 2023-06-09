package com.countries.vpn.AdsUtils.Utils;


import com.countries.vpn.AdsUtils.FirebaseADHandlers.AdsJsonPOJO;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;

import java.util.ArrayList;

public class Constants {
    public static String CURRENCY = "CURRENCY";
    public static String CURRENCY_STORED = "";
    public static boolean LIGHT_THEME = true;
    public static final String ADSJSON = "WHATS_TOOLS_TOPIC";
    public static AdsJsonPOJO adsJsonPOJO;
    public static int hitCounter = 0;
    public static ArrayList<InterstitialAd> InterstitialList = new ArrayList<>();
    public static ArrayList<AppOpenAd> AppOpenAdsList = new ArrayList<>();
    public static ArrayList<NativeAd> NativeAdsList = new ArrayList<>();
    public static ArrayList<AdView> CollapsibleAdsList = new ArrayList<>();
    public static final String IS_FIRST_RUN = "isFirstRun";

    public static boolean isNull(String val) {
        return val == null || val.trim().equalsIgnoreCase("") || val.trim().equalsIgnoreCase("null") || val.trim() == "" || val.trim() == "null";
    }

}
