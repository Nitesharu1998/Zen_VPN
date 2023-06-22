package com.countries.vpn.Fragment;

import static com.countries.vpn.AdsUtils.FirebaseADHandlers.MyApplication.getPreferences;
import static com.countries.vpn.AdsUtils.Utils.Constants.backend;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.countries.vpn.AdsUtils.FirebaseADHandlers.AdUtils;
import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.AdsUtils.Utils.Constants;
import com.countries.vpn.AdsUtils.Utils.DataViewModel;
import com.countries.vpn.AdsUtils.Utils.Global;
import com.countries.vpn.Vpn.CountryWiseVPNModels;
import com.countries.vpn.Vpn.PersistentConnectionProperties;
import com.countries.vpn.Vpn.TunnelModel;
import com.countries.vpn.Vpn.VPNInitiatorHandler;
import com.countries.vpn.Vpn.VpnInterfaces;
import com.countries.vpn.fastsecurevpnproxy.MainActivity;
import com.countries.vpn.fastsecurevpnproxy.R;
import com.countries.vpn.fastsecurevpnproxy.SelectVPNActivity;
import com.countries.vpn.fastsecurevpnproxy.TermsAndConditionsActivity;
import com.countries.vpn.fastsecurevpnproxy.databinding.FragmentHomeBinding;
import com.wireguard.android.backend.GoBackend;

import java.util.List;


public class HomeFragment extends Fragment {
    MainActivity mainActivity;
    private boolean isFirstImage = false;
    FragmentHomeBinding binding;
    ActionBarDrawerToggle toggle;
    DataViewModel viewModel;
    boolean isChecked = false;



    ActivityResultLauncher<Intent> openDialog = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                new Handler().post(() -> binding.imgPower.performClick());
            } else {
                Intent intentPrepare = GoBackend.VpnService.prepare(requireActivity());
                openDialog.launch(intentPrepare);
            }
        }
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        viewModel = new ViewModelProvider(this).get(DataViewModel.class);
        mainActivity = (MainActivity) requireActivity();
        AdUtils.showNativeAd(requireActivity(), binding.nativeAds, false);
        toggle = new ActionBarDrawerToggle(requireActivity(), binding.drawer, R.string.opendrawer, R.string.closedrawer);
        binding.imgPower.setClickable(false);
        binding.drawer.addDrawerListener(toggle);
        //startPromotionSwitch();
        toggle.syncState();
        binding.mNavigationView.bringToFront();
        setUpVPN();
        return binding.getRoot();
    }


   /* private void startPromotionSwitch() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFirstImage) {
                    binding.ivPromotionSwitch.setImageResource(R.drawable.ic_gopremium2);
                    isFirstImage = true;
                    startPromotionSwitch();
                } else {
                    isFirstImage = false;
                    binding.ivPromotionSwitch.setImageResource(R.drawable.ic_gopremium1);
                    startPromotionSwitch();
                }

            }
        }, 2000);
    }*/

    private void setUpVPN() {
        try {
            backend.getRunningTunnelNames();
        } catch (NullPointerException e) {
            // backend cannot be created without context
            PersistentConnectionProperties.getInstance().setBackend(new GoBackend(requireContext()));
            backend = PersistentConnectionProperties.getInstance().getBackend();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        binding.ivSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawer.close();
                Toast.makeText(mainActivity, "This feature will be available soon....", Toast.LENGTH_SHORT).show();
            }
        });

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawer.close();
            }
        });

        binding.nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawer.open();
            }
        });

        binding.ivConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.showInterstitialAd(requireActivity(), new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        binding.drawer.close();
                        startActivity(new Intent(requireActivity(), TermsAndConditionsActivity.class));
                    }
                });


            }
        });
        binding.ivRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.showInterstitialAd(requireActivity(), new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
                        binding.drawer.close();
                        final String appName = requireContext().getPackageName();
                        try {
                            requireContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
                        } catch (ActivityNotFoundException anfe) {
                            requireContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
                        }
                    }
                });


            }
        });
        binding.ivShareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawer.close();
                final String appLink = "\nhttps://play.google.com/store/apps/details?id=" + requireContext().getPackageName();
                Intent sendInt = new Intent(Intent.ACTION_SEND);
                sendInt.putExtra(Intent.EXTRA_SUBJECT, requireContext().getString(R.string.app_name));
                sendInt.setType("text/plain");
                requireContext().startActivity(Intent.createChooser(sendInt, "Share"));

            }
        });

        binding.rbAutoSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChecked = !isChecked;
                binding.rbAutoSelect.setChecked(isChecked);
            }
        });

        binding.imgPower.setOnClickListener(view -> {
            if (!Constants.isConnected) {
                binding.tvConnect.setText("Connecting...");
                Intent intentPrepare = GoBackend.VpnService.prepare(requireActivity());
                if (intentPrepare != null) {
                    openDialog.launch(intentPrepare);
                } else {
                    VPNInitiatorHandler.connectVPN(requireActivity(), Constants.randomTunnelModel, new VpnInterfaces.vpnConnectionInterface() {
                        @Override
                        public void isConnected(boolean state, String ipAddress) {
                            if (state) {
                                if (getPreferences().isFirstRun()) {
                                    MainActivity.showTapPrompt();
                                }
                                binding.llIpLayout.setVisibility(View.VISIBLE);
                                binding.rbAutoSelect.setVisibility(View.GONE);
                                binding.tvIpaddress.setText(ipAddress);
                                binding.tvConnect.setText("Connected");
                                binding.motionLayout.transitionToEnd();
                                binding.layout.setBackgroundResource(R.color.transperant);
                                getPreferences().setFirstRun(false);


                            } else {
                                binding.tvConnect.setText("Reconnecting...");
                                binding.motionLayout.transitionToStart();
                                new Handler().post(() -> binding.imgPower.performClick());
                                binding.layout.setBackgroundResource(R.color.appbg);
                               /* new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                }, 1000);*/
                            }
                        }

                    });
                }
            } else {
                binding.tvConnect.setText("Disconnected");
                VPNInitiatorHandler.disconnectVPN();
                binding.motionLayout.transitionToStart();
                binding.llIpLayout.setVisibility(View.GONE);
                binding.rbAutoSelect.setVisibility(View.VISIBLE);
                binding.layout.setBackgroundResource(R.color.appbg);
                Constants.isConnected = false;
            }


        });

        binding.rbAutoSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

            }
        });


        viewModel.getCountries().observe(this, new Observer<List<CountryWiseVPNModels>>() {
            @Override
            public void onChanged(List<CountryWiseVPNModels> countryWiseVPNModels) {
                if (countryWiseVPNModels.size() > 0) {
                    Constants.countryWiseVpnList = countryWiseVPNModels;
                    binding.imgPower.setClickable(true);
                }
            }
        });
        if (Constants.randomTunnelModel.getCountry() == null) {
            viewModel.getRandomTunnelModel().observe(this, new Observer<TunnelModel>() {
                @Override
                public void onChanged(TunnelModel model) {
                    if (model != null) {
                        binding.imgPower.setClickable(true);
                        Constants.randomTunnelModel = model;
                        binding.tvCountryName.setText(Constants.randomTunnelModel.getCountry());
                        Glide.with(requireActivity()).load(Global.getFlagOfCountry(Constants.randomTunnelModel.getCountry())).into(binding.imgVpnFlag);
                        binding.tvConnect.setText("Disconnected");
                        binding.motionLayout.transitionToStart();
                        binding.layout.setBackgroundResource(R.color.appbg);
                        onResume();
                    }
                }
            });
        } else if (Constants.isConnected) {
            binding.llIpLayout.setVisibility(View.VISIBLE);
            binding.rbAutoSelect.setVisibility(View.GONE);
            binding.tvIpaddress.setText(Constants.IPAddress);
            binding.tvConnect.setText("Connected");
            binding.tvCountryName.setText(Constants.randomTunnelModel.getCountry());
            Glide.with(requireActivity()).load(Global.getFlagOfCountry(Constants.randomTunnelModel.getCountry())).into(binding.imgVpnFlag);
            binding.motionLayout.transitionToEnd();
            binding.layout.setBackgroundResource(R.color.transperant);
        } else {
            binding.imgPower.setClickable(true);
            VPNInitiatorHandler.disconnectVPN();
            binding.tvCountryName.setText(Constants.randomTunnelModel.getCountry());
            Glide.with(requireActivity()).load(Global.getFlagOfCountry(Constants.randomTunnelModel.getCountry())).into(binding.imgVpnFlag);
            binding.motionLayout.transitionToStart();
            binding.layout.setBackgroundResource(R.color.appbg);
        }

        binding.cvCardSuggestedServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), SelectVPNActivity.class));
            }
        });


        binding.ivBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.launchBrowser();
                binding.drawer.close();
            }
        });
        binding.ivVpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawer.close();
            }
        });


    }
}