package com.countries.vpn.Vpn;

import static com.countries.vpn.fastsecurevpnproxy.SplashActivity.backend;
import static com.wireguard.android.backend.Tunnel.State.DOWN;
import static com.wireguard.android.backend.Tunnel.State.UP;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.countries.vpn.AdsUtils.Utils.ConnectionDetector;
import com.countries.vpn.Vpn.APIHandler.KeysModel;
import com.countries.vpn.Vpn.APIHandler.PostAPIInterfaces;
import com.countries.vpn.Vpn.APIHandler.RetroFit_APIClient;
import com.countries.vpn.fastsecurevpnproxy.R;
import com.wireguard.android.backend.Tunnel;
import com.wireguard.config.Config;
import com.wireguard.config.InetEndpoint;
import com.wireguard.config.InetNetwork;
import com.wireguard.config.Interface;
import com.wireguard.config.Peer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VPNInitiatorHandler {
    int randomNumber;
    static Activity activity;
    static TunnelModel model;
    static VpnInterfaces.vpnConnectionInterface vpnInterfaces;
    int retries;
    static ArrayList<KeysModel> keysModel;


    public VPNInitiatorHandler(Activity context, VpnInterfaces.vpnConnectionInterface vpnInterfaces) {
        this.activity = context;
        this.vpnInterfaces = vpnInterfaces;
        callAuthorizationApi();
        //callApi(keysModel.get(randomNumber).getKey());
    }

    private static void callAuthorizationApi() {
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

    private static void callApi(String key) {
        PostAPIInterfaces apiInterfaces = RetroFit_APIClient.getInstance().getClient(activity.getString(R.string.VPN_BASEURL), key).create(PostAPIInterfaces.class);
        Call<ArrayList<TunnelModel>> call = apiInterfaces.CallServerApi(activity.getPackageName());
        call.enqueue(new Callback<ArrayList<TunnelModel>>() {
            @Override
            public void onResponse(Call<ArrayList<TunnelModel>> call, Response<ArrayList<TunnelModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<TunnelModel> responseModel = validateKeysModel(response.body());
                    if (!responseModel.isEmpty()) {
                        model = responseModel.get(new Random().nextInt(responseModel.size()));
                        connectVPN(model);
                    } else {
                        vpnInterfaces.isConnected(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TunnelModel>> call, Throwable t) {
                callAuthorizationApi();
            }
        });
    }


    public static void disconnectVPN() {
        Tunnel tunnel = PersistentConnectionProperties.getInstance().getTunnel();

        AsyncTask.execute(() -> {
            try {
                if (backend.getState(PersistentConnectionProperties.getInstance().getTunnel()) == UP) {
                    backend.setState(tunnel, DOWN, null);
                }
            } catch (Exception e) {
                Log.e("vpnTest", e.toString());
            }
        });
    }

    public static void connectVPN(TunnelModel model) {
        ConnectionDetector cd = new ConnectionDetector(activity);
        Random random = new Random();
        String[] addresses = model.getAddress().split(",");
        String[] allowedIpArray = model.getAllowed_ips().split(",");
        Tunnel tunnel = PersistentConnectionProperties.getInstance().getTunnel();
        Interface.Builder interfaceBuilder = new Interface.Builder();
        Peer.Builder peerBuilder = new Peer.Builder();

        new Handler().post(() -> {
            try {
                if (backend.getState(tunnel) == DOWN) {
                    backend.setState(tunnel, UP, new Config.Builder().setInterface(interfaceBuilder.addAddress(InetNetwork.parse(addresses[0])).parsePrivateKey(model.getPrivatekey()).build()).addPeer(peerBuilder.addAllowedIps(Collections.singleton(InetNetwork.parse(allowedIpArray[0]))).parsePreSharedKey(model.getPresharedkey()).setEndpoint(InetEndpoint.parse(model.getEndpoints())).parsePublicKey(model.getPublickey()).build()).build());
                    vpnInterfaces.isConnected(true);
                }
            } catch (Exception e) {
                callAuthorizationApi();
            }
        });
    }

}
