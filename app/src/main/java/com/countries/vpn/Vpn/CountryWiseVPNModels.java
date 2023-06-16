package com.countries.vpn.Vpn;

import java.util.ArrayList;

public class CountryWiseVPNModels {
    String countryName;
    ArrayList<TunnelModel> tunnelModels = new ArrayList<>();

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }


    public ArrayList<TunnelModel> getModelsList() {
        return tunnelModels;
    }

    public void setModelsList(ArrayList<TunnelModel> modelsList) {
        this.tunnelModels = modelsList;
    }

    public void addTunnelModel(TunnelModel singleModel) {
        this.tunnelModels.add(singleModel);
    }

}
