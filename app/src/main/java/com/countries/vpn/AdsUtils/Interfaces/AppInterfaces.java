package com.countries.vpn.AdsUtils.Interfaces;

import android.graphics.Bitmap;

import com.countries.vpn.AdsUtils.FirebaseADHandlers.AdsJsonPOJO;
import com.countries.vpn.AdsUtils.Utils.CricketFlagsResponseModel;
import com.countries.vpn.Vpn.TunnelModel;

import java.util.ArrayList;

public class AppInterfaces {
    public interface AdDataInterface {
        void getAdData(AdsJsonPOJO adsJsonPOJO);
    }

    public interface InterstitialADInterface {
        void adLoadState(boolean isLoaded);
    }

    public interface AppOpenADInterface {
        void appOpenAdState(boolean state_load);
    }

    public interface WebViewInterface {
        void getClickedURL(String URL);

        void getBitmap(Bitmap imageMap);
    }

    public interface AdapterClickInterface {
        void onClick(int position);
    }

    public interface CollapsibleAdInterface {
        void setAdState(boolean adState); //true=ad_open, false=ad_closed
    }

    public interface AdapterClick {
        void clickedPosition(int position);
    }

    public interface getVPNModel {
        void vpnResponseModel(ArrayList<TunnelModel> tunnelModelList);
    }

    public interface FlagsInterface {
        void setFlagsData(CricketFlagsResponseModel cricketFlagsResponseModel);
    }

    public interface HomeFragmentClick {
        void getClick(boolean isVpn);
    }

    public interface TabAdapterInterface {
        void removeTab(int Position);

        void hitLink(int Position);

        void addNewTab();
    }
}
