package com.countries.vpn.AdsUtils.Utils;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.Vpn.APIHandler.VPNApiCallHandler;
import com.countries.vpn.Vpn.CountryWiseVPNModels;
import com.countries.vpn.Vpn.TunnelModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataViewModel extends AndroidViewModel {

    MutableLiveData<List<CountryWiseVPNModels>> countriesModel = new MutableLiveData<>();
    MutableLiveData<TunnelModel> randomTunnelModel = new MutableLiveData<>();

    public DataViewModel(@NonNull Application application) {
        super(application);
        loadData();
    }

    private void loadData() {
        new VPNApiCallHandler(getApplication().getApplicationContext(), new AppInterfaces.getVPNModel() {
            @Override
            public void vpnResponseModel(ArrayList<TunnelModel> tunnelModelList) {
                Random test = new Random();
                int random = test.nextInt(tunnelModelList.size());
                Constants.tunnelModelList = tunnelModelList;
                countriesModel.setValue(Global.getCountryWiseVPNList(tunnelModelList));
                randomTunnelModel.setValue(tunnelModelList.get(random));
            }
        });
    }

    public LiveData<List<CountryWiseVPNModels>> getCountries() {
        return countriesModel;
    }

    public LiveData<TunnelModel> getRandomTunnelModel() {
        return randomTunnelModel;
    }

}
