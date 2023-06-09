package com.countries.vpn.Vpn.APIHandler;


import com.countries.vpn.Vpn.TunnelModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PostAPIInterfaces {

    @FormUrlEncoded
    @POST("api/allvpn/")
    Call<ArrayList<TunnelModel>> CallServerApi(@Field("packagename") String packageName);

    @FormUrlEncoded
    @POST("apitoken/alltokens/")
    Call<ArrayList<KeysModel>> CallAuthApi(@Field("packagename") String packageName);
}
