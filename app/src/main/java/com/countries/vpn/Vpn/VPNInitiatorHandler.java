package com.countries.vpn.Vpn;

import static com.countries.vpn.AdsUtils.Utils.Constants.backend;
import static com.wireguard.android.backend.Tunnel.State.DOWN;
import static com.wireguard.android.backend.Tunnel.State.UP;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.countries.vpn.AdsUtils.Utils.ConnectionDetector;
import com.countries.vpn.AdsUtils.Utils.Constants;
import com.wireguard.android.backend.Tunnel;
import com.wireguard.config.Config;
import com.wireguard.config.InetEndpoint;
import com.wireguard.config.InetNetwork;
import com.wireguard.config.Interface;
import com.wireguard.config.Peer;

import java.util.Collections;
import java.util.Random;

public class VPNInitiatorHandler {

    static VpnInterfaces.vpnConnectionInterface vpnInterfaces;

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


    public static void connectVPN(Activity activity, TunnelModel model, VpnInterfaces.vpnConnectionInterface vpnInterfaces) {
        ConnectionDetector cd = new ConnectionDetector(activity);
        Random random = new Random();
        String[] addresses = model.getAddress().split(",");
        String[] allowedIpArray = model.getAllowed_ips().split(",");
        Tunnel tunnel = PersistentConnectionProperties.getInstance().getTunnel();
        Interface.Builder interfaceBuilder = new Interface.Builder();
        Peer.Builder peerBuilder = new Peer.Builder();

        if (cd.isConnectingToInternet()) {
            new Handler().post(() -> {
                try {
                    backend.setState(tunnel, UP, new Config.Builder().setInterface(interfaceBuilder.addAddress(InetNetwork.parse(addresses[0])).parsePrivateKey(model.getPrivatekey()).build()).addPeer(peerBuilder.addAllowedIps(Collections.singleton(InetNetwork.parse(allowedIpArray[0]))).parsePreSharedKey(model.getPresharedkey()).setEndpoint(InetEndpoint.parse(model.getEndpoints())).parsePublicKey(model.getPublickey()).build()).build());
                    vpnInterfaces.isConnected(true, addresses[0]);
                    Constants.isConnected = true;
                    Constants.IPAddress = addresses[0];
                } catch (Exception e) {
                    try {
                        backend.setState(tunnel, UP, new Config.Builder().setInterface(interfaceBuilder.addAddress(InetNetwork.parse(addresses[0])).parsePrivateKey(model.getPrivatekey()).build()).addPeer(peerBuilder.addAllowedIps(Collections.singleton(InetNetwork.parse(allowedIpArray[0]))).parsePreSharedKey(model.getPresharedkey()).setEndpoint(InetEndpoint.parse(model.getEndpoints())).parsePublicKey(model.getPublickey()).build()).build());
                        vpnInterfaces.isConnected(true, addresses[0]);
                        Constants.isConnected = true;
                        Constants.IPAddress = addresses[0];
                    } catch (Exception ex) {
                        vpnInterfaces.isConnected(false, "");
                        Constants.isConnected = false;
                        Constants.IPAddress = null;
                    }
                }
            });
        }
    }

}
