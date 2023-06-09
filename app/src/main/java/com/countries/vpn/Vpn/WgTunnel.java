package com.countries.vpn.Vpn;

import androidx.annotation.NonNull;

import com.wireguard.android.backend.Tunnel;

public class WgTunnel implements Tunnel {

    @NonNull
    @Override
    public String getName() {
        return "wgpreconf1";
    }

    @Override
    public void onStateChange(@NonNull State newState) {
    }
}