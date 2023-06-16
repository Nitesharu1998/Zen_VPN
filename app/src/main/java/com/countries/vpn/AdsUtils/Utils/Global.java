package com.countries.vpn.AdsUtils.Utils;

import static android.os.Build.VERSION.SDK_INT;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.countries.vpn.AdsUtils.FirebaseADHandlers.AdsJsonPOJO;
import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.AdsUtils.PreferencesManager.AppPreferences;
import com.countries.vpn.CommonDataModels.WebLinksDataModel;
import com.countries.vpn.Vpn.CountryWiseVPNModels;
import com.countries.vpn.Vpn.TunnelModel;
import com.countries.vpn.fastsecurevpnproxy.BuildConfig;
import com.countries.vpn.fastsecurevpnproxy.R;
import com.countries.vpn.fastsecurevpnproxy.databinding.AddBookmarkLayoutBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Global {
    public static final String PLEASE_WAIT = "We are processing your request...";
    public static ArrayList<String> mSelectedImgPath = new ArrayList<>();

    private Context context;

    public Global(Context context) {
        this.context = context;
    }

    static AlertDialog alertDialog;
    private static ProgressDialog dialog;


    public static void hideProgressDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }

            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
                alertDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sout(String TagToString, Object whatToPrint) {
        if (BuildConfig.DEBUG) {
            System.out.println(TagToString + " " + whatToPrint);
        }
    }


    public static AdsJsonPOJO getAdsData(String json) {
        Type familyType = new TypeToken<AdsJsonPOJO>() {
        }.getType();
        return new Gson().fromJson(json, familyType);

    }

    public static long mLastClickTime = 0;

    public static void checkClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    public static boolean isLatestVersion() {
        return SDK_INT >= Build.VERSION_CODES.R;
    }

    public static Uri getContentMediaUri() {
        if (isLatestVersion()) {
            return MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
    }

    public static void printLog(String key, String val) {
        if (BuildConfig.DEBUG) Log.e(key + "*===", "===*" + val);
    }

    public static boolean isEmptyStr(String str) {
        if (str == null || str.trim().isEmpty() || str.equalsIgnoreCase("")) return true;
        return false;
    }

    public static String getRootFileForSave() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        } else {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
    }

    public static Uri getUri(File file, Context context) {
        Uri contentUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contentUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        } else {
            contentUri = Uri.fromFile(file);
        }
        return contentUri;
    }

    public static boolean isArrayListNull(ArrayList arrayList) {
        try {
            if (arrayList == null) {
                return true;
            } else if (arrayList != null && arrayList.size() <= 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static CricketFlagsResponseModel setUpFlagsModel(String json) {
        return new Gson().fromJson(json, new TypeToken<CricketFlagsResponseModel>() {
        }.getType());
    }

    public static boolean isClassNull(Object objectToCheck) {
        return objectToCheck == null;
    }

    public static String getFlagOfCountry(String countryName) {
        if (!Global.isArrayListNull(Constants.cricketFlagsModel.getFlagList()) && !isClassNull(Constants.cricketFlagsModel)) {
            for (int i = 0; i < Constants.cricketFlagsModel.getFlagList().size(); i++) {
                if (Constants.cricketFlagsModel.getFlagList().get(i).getFlagShort().contains(countryName)) {
                    return Constants.cricketFlagsModel.getFlagList().get(i).getFlagURL();
                }
            }
        }
        return "";
    }

    public static List<CountryWiseVPNModels> getCountryWiseVPNList(ArrayList<TunnelModel> tunnelModelList) {
        HashSet<String> country = new HashSet<>();
        List<CountryWiseVPNModels> list = new ArrayList<>();
        CountryWiseVPNModels countryWiseVPNModels;


        for (TunnelModel model : tunnelModelList) {
            country.add(model.getCountry());
        }

        if (country.size() > 0) {
            for (String c : country) {
                countryWiseVPNModels = new CountryWiseVPNModels();
                for (TunnelModel model : tunnelModelList) {
                    if (c.equals(model.getCountry())) {
                        countryWiseVPNModels.setCountryName(model.getCountry());
                        countryWiseVPNModels.addTunnelModel(model);
                    }
                }
                list.add(countryWiseVPNModels);
            }
        }

        return list;
    }

    public static ArrayList<WebLinksDataModel> getWebLinkDataModel(Context context) {
        ArrayList<WebLinksDataModel> testModel = new ArrayList<>();
        AppPreferences preferences = new AppPreferences(context);
        testModel = new Gson().fromJson(preferences.getWebLinkModel(), new TypeToken<ArrayList<WebLinksDataModel>>() {
        }.getType());
        if (isArrayListNull(testModel)) {
            return new ArrayList<WebLinksDataModel>();
        } else {
            return testModel;
        }
    }

    public static void loadURLinWebView(WebView wvMain, String urlToHit, ImageView ivBackPress, ImageView ivNextPress, AppInterfaces.WebViewInterface webViewInterface) {
        wvMain.loadUrl(urlToHit);
        wvMain.setWebViewClient(new WebViewClient());
        wvMain.getSettings().setJavaScriptEnabled(true);
        wvMain.getSettings().setUseWideViewPort(true);
        wvMain.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webViewInterface.getClickedURL(url);
                webViewInterface.getBitmap(favicon);
            }

        });

        ivBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wvMain.canGoBack()) wvMain.goBack();
            }
        });
        ivNextPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wvMain.canGoForward()) wvMain.goForward();
            }
        });
    }

    public static void openAddBookMarkDialog(Activity activity, String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AddBookmarkLayoutBinding addBookmarkLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.add_bookmark_layout, null, false);
        builder.setView(addBookmarkLayoutBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        addBookmarkLayoutBinding.edtBookmarkUrl.setText(url);
        addBookmarkLayoutBinding.btnCancel.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        addBookmarkLayoutBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPreferences preferences = new AppPreferences(activity);
                ArrayList<WebLinksDataModel> model = Global.getWebLinkDataModel(activity);
                ArrayList<WebLinksDataModel> finalModel = new ArrayList<>();
                WebLinksDataModel singleEntry = new WebLinksDataModel();
                if (validateBookmark(addBookmarkLayoutBinding.edtBookmarkUrl.getText().toString(), addBookmarkLayoutBinding.edtBookmarkName.getText().toString())) {
                    singleEntry.setName(addBookmarkLayoutBinding.edtBookmarkName.getText().toString());
                    singleEntry.setURL(addBookmarkLayoutBinding.edtBookmarkUrl.getText().toString());
                    model.add(singleEntry);
                    preferences.setWebLinkModel(model);
                    dialog.dismiss();
                }
            }

            private boolean validateBookmark(String url, String name) {
                if (!URLUtil.isValidUrl(url)) {
                    Toast.makeText(activity, "Please Enter Valid URL", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (name == null || name.isEmpty()) {
                    Toast.makeText(activity, "Please Enter Name For Bookmark", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });

        addBookmarkLayoutBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

}
