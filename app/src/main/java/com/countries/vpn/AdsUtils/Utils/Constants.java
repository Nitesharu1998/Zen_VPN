package com.countries.vpn.AdsUtils.Utils;


import com.countries.vpn.AdsUtils.FirebaseADHandlers.AdsJsonPOJO;
import com.countries.vpn.Vpn.CountryWiseVPNModels;
import com.countries.vpn.Vpn.TunnelModel;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.wireguard.android.backend.Backend;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String URL_INTENT = "URL_INTENT";
    public static final String WEBLINKMODEL = "WEBLINKMODEL";
    public static final String NORMAL_TABS = "NORMAL_TABS";
    public static final String PRIVATE_TABS = "PRIVATE_TABS";
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
    public static boolean isConnected = false;
    public static String FlagsModel = "FlagsModel";
    public static CricketFlagsResponseModel cricketFlagsModel = new CricketFlagsResponseModel();
    public static ArrayList<TunnelModel> tunnelModelList = new ArrayList<>();
    public static TunnelModel randomTunnelModel = new TunnelModel();
    public static List<CountryWiseVPNModels> countryWiseVpnList = new ArrayList<>();
    public static Backend backend;

    public static ArrayList<String> privateLinksArray = new ArrayList<>();
    public static ArrayList<String> publicLinksArray = new ArrayList<>();

    public static final String FACEBOOK = "https://www.facebook.com/";
    public static final String GOOGLE = "https://www.google.com/";
    public static final String INSTAGRAM = "https://www.instagram.com/";
    public static final String CRICINFO = "https://m.cricbuzz.com/";
    public static final String STOCK = "https://www.nseindia.com/";
    public static final String NEWS = "https://edition.cnn.com/";
    public static boolean isPrivateTab=false;

    public static boolean isNull(String val) {
        return val == null || val.trim().equalsIgnoreCase("") || val.trim().equalsIgnoreCase("null") || val.trim() == "" || val.trim() == "null";
    }

}
