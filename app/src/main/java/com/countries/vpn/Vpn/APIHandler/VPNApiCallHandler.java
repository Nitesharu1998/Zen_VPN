package com.countries.vpn.Vpn.APIHandler;

import android.content.Context;
import android.util.Log;

import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.Vpn.TunnelModel;
import com.countries.vpn.Vpn.VpnValidator;
import com.countries.vpn.fastsecurevpnproxy.R;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VPNApiCallHandler {
    Context activity;
    static TunnelModel model;
    static ArrayList<KeysModel> keysModel;
    AppInterfaces.getVPNModel modelInterface;

    public VPNApiCallHandler(Context activity, AppInterfaces.getVPNModel modelInterface) {
        this.activity = activity;
        this.modelInterface = modelInterface;
        callAuthorizationApi();
    }

    private void callAuthorizationApi() {
        PostAPIInterfaces apiInterfaces = RetroFit_APIClient.getInstance().getClient(activity.getString(R.string.VPN_BASEURL), "d4bc7d48da6096ab6463af3f0495033ed3c69c27").create(PostAPIInterfaces.class);
        Call<ArrayList<KeysModel>> call = apiInterfaces.CallAuthApi(activity.getPackageName());
        call.enqueue(new Callback<ArrayList<KeysModel>>() {
            @Override
            public void onResponse(Call<ArrayList<KeysModel>> call, Response<ArrayList<KeysModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    keysModel = response.body();
                    if (keysModel.size() > 0) {
                        Random random = new Random();
                        callApi(keysModel.get(random.nextInt(keysModel.size())).getKey());
                    } else {
                        callAuthorizationApi();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<KeysModel>> call, Throwable t) {
                callAuthorizationApi();
            }
        });
    }

    private void callApi(String key) {
        PostAPIInterfaces apiInterfaces = RetroFit_APIClient.getInstance().getClient(activity.getString(R.string.VPN_BASEURL), key).create(PostAPIInterfaces.class);
        Call<ArrayList<TunnelModel>> call = apiInterfaces.CallServerApi(activity.getPackageName());
        call.enqueue(new Callback<ArrayList<TunnelModel>>() {
            @Override
            public void onResponse(Call<ArrayList<TunnelModel>> call, Response<ArrayList<TunnelModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<TunnelModel> responseModel = validateKeysModel(response.body());
                    if (!responseModel.isEmpty()) {
                        modelInterface.vpnResponseModel(responseModel);
                    } else {
                        Log.e("onResponse: ", "repsonse model was empty");
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TunnelModel>> call, Throwable t) {
                callAuthorizationApi();
            }
        });
    }

    private static ArrayList<TunnelModel> validateKeysModel(ArrayList<TunnelModel> rawResponseModel) {
        if (rawResponseModel != null) {
            ArrayList<TunnelModel> filteredModel = new ArrayList<>();
            for (int i = 0; i < rawResponseModel.size(); i++) {
                if (VpnValidator.validateDNS(rawResponseModel.get(i).getDns()) && VpnValidator.validateIpAddress(rawResponseModel.get(i).getAddress())) {
                    filteredModel.add(rawResponseModel.get(i));
                }
            }
            return filteredModel;
        }
        return null;
    }

}
