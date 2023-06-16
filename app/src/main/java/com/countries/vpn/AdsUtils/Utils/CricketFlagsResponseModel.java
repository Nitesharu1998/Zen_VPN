package com.countries.vpn.AdsUtils.Utils;

import java.util.ArrayList;

public class CricketFlagsResponseModel {
    public ArrayList<CricketFlagsResponseModel.FlagList> getFlagList() {
        return FlagList;
    }

    public void setFlagList(ArrayList<CricketFlagsResponseModel.FlagList> flagList) {
        FlagList = flagList;
    }

    ArrayList<FlagList> FlagList = new ArrayList<>();

    public class FlagList {
        String FlagName, FlagURL, FlagShort;

        public String getFlagShort() {
            return FlagShort;
        }

        public void setFlagShort(String flagShort) {
            FlagShort = flagShort;
        }

        public String getFlagURL() {
            return FlagURL;
        }

        public void setFlagURL(String flagURL) {
            FlagURL = flagURL;
        }

        public String getFlagName() {
            return FlagName;
        }

        public void setFlagName(String flagName) {
            FlagName = flagName;
        }
    }
}
