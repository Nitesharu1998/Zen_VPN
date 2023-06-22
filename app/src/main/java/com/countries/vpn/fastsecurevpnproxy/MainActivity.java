package com.countries.vpn.fastsecurevpnproxy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.countries.vpn.AdsUtils.FirebaseADHandlers.AdUtils;
import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.AdsUtils.Utils.Constants;
import com.countries.vpn.Fragment.HomeFragment;
import com.countries.vpn.Fragment.SpeedTestFragment;
import com.countries.vpn.Fragment.WebSurfingFragment;
import com.countries.vpn.fastsecurevpnproxy.databinding.ActivityMainBinding;
import com.countries.vpn.fastsecurevpnproxy.databinding.BackAlertBinding;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

public class MainActivity extends AppCompatActivity {
    static ActivityMainBinding binding;
    static Activity activity;
    private boolean mIsDoubleClick = false;

    @Override
    public void onBackPressed() {
        openBackAlertDialog();
    }


    private void openBackAlertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        BackAlertBinding backAlertBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.back_alert, null, false);
        dialog.setView(backAlertBinding.getRoot());
        AlertDialog dialog1 = dialog.create();
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();
        backAlertBinding.btnBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                launchBrowser();
            }
        });
        backAlertBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                finish();
            }
        });

    }

    public static void switchClicks(boolean shouldAllow) {
        binding.ivBrowser.setClickable(shouldAllow);
        binding.ivVpn.setClickable(shouldAllow);
        binding.ivSpeedtest.setClickable(shouldAllow);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_main);
        startService(new Intent(activity, VpnService.class));
        initListeners();
        if (Constants.isPrivateTab) {
            refreshFragment(new WebSurfingFragment());
        } else refreshFragment(new HomeFragment());

    }

    public void launchBrowser() {
        binding.ivBrowser.setImageResource(R.drawable.ic_bottom_browser_selected);
        binding.ivVpn.setImageResource(R.drawable.ic_bottom_vpn);
        binding.ivSpeedtest.setImageResource(R.drawable.ic_bottom_speedtest);
        refreshFragment(new WebSurfingFragment());
    }

    public static void showTapPrompt() {
        binding.ivBrowser.setImageResource(R.drawable.ic_bottom_browser_tap_target);
        new MaterialTapTargetPrompt.Builder(activity).setPromptFocal(new RectanglePromptFocal())
                .setPromptBackground(new RectanglePromptBackground()).setBackgroundColour(activity.getResources().getColor(R.color.startcolor)).setTarget(binding.ivBrowser).setSecondaryText("Click on Browser button to access safer browsing with VPN")
                .setPromptStateChangeListener((prompt, state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state== MaterialTapTargetPrompt.STATE_DISMISSED) {
                        // User has pressed the prompt target
                        binding.ivBrowser.setImageResource(R.drawable.ic_bottom_browser);
                        prompt.dismiss();
                    }
                }).show();
    }

    private void initListeners() {
        binding.ivBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* if (Constants.isConnected) {*/
                AdUtils.showInterstitialAd(activity, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        launchBrowser();
                    }
                });
            }
        });
        binding.ivVpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* if (Constants.isConnected) {*/
                AdUtils.showInterstitialAd(activity, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        binding.ivVpn.setImageResource(R.drawable.ic_bottom_vpn_selected);
                        binding.ivBrowser.setImageResource(R.drawable.ic_bottom_browser);
                        binding.ivSpeedtest.setImageResource(R.drawable.ic_bottom_speedtest);

                        refreshFragment(new HomeFragment());
                    }
                });
//                } else {
//                    Toast.makeText(activity, "Please connect to the VPN service", Toast.LENGTH_SHORT).show();
//                }


            }
        });
        binding.ivSpeedtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (Constants.isConnected) {*/
                AdUtils.showInterstitialAd(activity, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        binding.ivSpeedtest.setImageResource(R.drawable.ic_bottom_speedtest_selected);
                        binding.ivBrowser.setImageResource(R.drawable.ic_bottom_browser);
                        binding.ivVpn.setImageResource(R.drawable.ic_bottom_vpn);
                        refreshFragment(new SpeedTestFragment());
                    }
                });
                /*} else {
                    Toast.makeText(activity, "Please connect to the VPN service", Toast.LENGTH_SHORT).show();
                }*/

            }
        });
    }

    public void refreshFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frm_container, fragment).commit();
    }


}