package com.countries.vpn.AdsUtils.Utils;

import android.content.Context;
import android.os.AsyncTask;

import com.countries.vpn.AdsUtils.FirebaseADHandlers.FirebaseUtils;
import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;

public class AppAsyncTasks {

    public static class GetFlags extends AsyncTask<Void, Void, Void> {
        Context context;

        public GetFlags(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseUtils.getFlagsFromFirebase(context, new AppInterfaces.FlagsInterface() {
                @Override
                public void setFlagsData(CricketFlagsResponseModel cricketFlagsResponseModel) {
                    Constants.cricketFlagsModel = cricketFlagsResponseModel;
                }
            });
            return null;
        }
    }
}
