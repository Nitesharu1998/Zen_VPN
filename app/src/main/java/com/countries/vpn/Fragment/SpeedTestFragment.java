package com.countries.vpn.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.countries.vpn.AdsUtils.FirebaseADHandlers.AdUtils;
import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.AdsUtils.Utils.Constants;
import com.countries.vpn.AdsUtils.Utils.Global;
import com.countries.vpn.SpeedHandlers.GetSpeedTestHostsHandler;
import com.countries.vpn.SpeedHandlers.HttpDownloadTest;
import com.countries.vpn.SpeedHandlers.HttpUploadTest;
import com.countries.vpn.SpeedHandlers.PingTest;
import com.countries.vpn.fastsecurevpnproxy.MainActivity;
import com.countries.vpn.fastsecurevpnproxy.R;
import com.countries.vpn.fastsecurevpnproxy.databinding.FragmentSpeedTestBinding;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SpeedTestFragment extends Fragment {
    static int position = 0;
    static int lastPosition = 0;
    final DecimalFormat dec = new DecimalFormat("#.##");
    Context context;
    FragmentSpeedTestBinding binding;
    GetSpeedTestHostsHandler getSpeedTestHostsHandler = null;
    HashSet<String> tempBlackList = new HashSet<>();
    TextView tv_loc1, tv_loc2, sim, get_ip;
    LocationManager locationManager;
    PingTest pingTest;
    HttpDownloadTest downloadTest;
    HttpUploadTest uploadTest;
    MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.fragment_speed_test, container, false);
        context = binding.getRoot().getContext();
        
        activity = (MainActivity) requireActivity();

        if (Constants.randomTunnelModel.getCountry() != null) {
            binding.tvCountryName.setText(Constants.randomTunnelModel.getCountry());
            Glide.with(context).load(Global.getFlagOfCountry(Constants.randomTunnelModel.getCountry())).into(binding.imgVpnFlag);
        } else {
            binding.cvCardSuggestedServer.setVisibility(View.GONE);
        }
        checkLocationisEnabledOrNot();
        getLocation();
        getIpAddress();
        binding.tvStartRestartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdUtils.showInterstitialAd(activity, new AppInterfaces.InterstitialADInterface() {
                    @Override
                    public void adLoadState(boolean isLoaded) {
//                        switchClicks(false);
                        startTest();
                    }
                });

            }
        });

        return binding.getRoot();
    }

    private void startTest() {
        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        getSpeedTestHostsHandler.start();

        new Thread(new Runnable() {
            RotateAnimation rotate;

            @Override
            public void run() {
                activity.runOnUiThread(() -> binding.tvStartRestartTest.setText("Selecting best server"));
                int timeCount = 600;
                while (!getSpeedTestHostsHandler.isFinished()) {
                    timeCount--;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    if (timeCount <= 0) {
                        activity.runOnUiThread(() -> {
                            Toast.makeText(context, "No Connection...", Toast.LENGTH_LONG).show();
                            binding.tvStartRestartTest.setEnabled(true);
                            binding.tvStartRestartTest.setTextSize(16);
                            binding.tvStartRestartTest.setText("Restart Test");
                        });
                        getSpeedTestHostsHandler = null;
                        return;
                    }
                }
                HashMap<Integer, String> mapKey = getSpeedTestHostsHandler.getMapKey();
                HashMap<Integer, List<String>> mapValue = getSpeedTestHostsHandler.getMapValue();
                double selfLat = getSpeedTestHostsHandler.getSelfLat();
                double selfLon = getSpeedTestHostsHandler.getSelfLon();
                double tmp = 19349458;
                double dist = 0.0;
                int findServerIndex = 0;
                for (int index : mapKey.keySet()) {
                    if (tempBlackList.contains(mapValue.get(index).get(5))) {
                        continue;
                    }
                    Location source = new Location("Source");
                    source.setLatitude(selfLat);
                    source.setLongitude(selfLon);
                    List<String> ls = mapValue.get(index);
                    Location dest = new Location("Dest");
                    dest.setLatitude(Double.parseDouble(ls.get(0)));
                    dest.setLongitude(Double.parseDouble(ls.get(1)));
                    double distance = source.distanceTo(dest);
                    if (tmp > distance) {
                        tmp = distance;
                        dist = distance;
                        findServerIndex = index;
                    }
                }
                String testAddr = mapKey.get(findServerIndex).replace("http://", "https://");
                final List<String> info = mapValue.get(findServerIndex);
                final double distance = dist;

                if (info == null) {
                    activity.runOnUiThread(() -> {
                        binding.tvStartRestartTest.setTextSize(12);
                        binding.tvStartRestartTest.setText("There was a problem in getting Host Location. Try again later.");
                    });
                    return;
                }
                activity.runOnUiThread(() -> {
                    binding.tvStartRestartTest.setTextSize(13);
                    binding.tvStartRestartTest.setText(String.format("Host Location: %s ", info.get(2)));
                });
                activity.runOnUiThread(() -> {
                    binding.tvDownloadSpeed.setText("0");
                    binding.tvUploadSpeed.setText("0");
                });
                final List<Double> pingRateList = new ArrayList<>();
                final List<Double> downloadRateList = new ArrayList<>();
                final List<Double> uploadRateList = new ArrayList<>();
                Boolean pingTestStarted = false;
                Boolean pingTestFinished = false;
                Boolean downloadTestStarted = false;
                Boolean downloadTestFinished = false;
                Boolean uploadTestStarted = false;
                Boolean uploadTestFinished = false;
                pingTest = new PingTest(info.get(6).replace(":8080", ""), 3);
                downloadTest = new HttpDownloadTest(testAddr.replace(testAddr.split("/")[testAddr.split("/").length - 1], ""));
                uploadTest = new HttpUploadTest(testAddr);
                while (true) {

                    if (!pingTestStarted) {
                        pingTest.start();
                        pingTestStarted = true;
                    }
                    if (pingTestFinished && !downloadTestStarted) {
                        downloadTest.start();
                        downloadTestStarted = true;
                    }
                    if (downloadTestFinished && !uploadTestStarted) {
                        uploadTest.start();
                        uploadTestStarted = true;
                    }
                    if (pingTestFinished) {
                        if (pingTest.getAvgRtt() == 0) {
                            System.out.println("Ping error...");
                        } else {
                            activity.runOnUiThread(() -> binding.tvPingSpeed.setText("" + pingTest.getAvgRtt() + "\nms"));
                        }
                    } else {
                        pingRateList.add(pingTest.getInstantRtt());

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                        activity.runOnUiThread(() -> {
                            XYSeries pingSeries = new XYSeries("");
                            pingSeries.setTitle("");

                            int count = 0;
                            List<Double> tmpLs = new ArrayList<>(pingRateList);
                            for (Double val : tmpLs) {
                                pingSeries.add(count++, val);
                            }

                            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                            dataset.addSeries(pingSeries);
                        });
                    }
                    if (pingTestFinished) {
                        if (downloadTestFinished) {
                            if (downloadTest.getFinalDownloadRate() == 0) {
                                System.out.println("Download error...");
                            } else {
                                activity.runOnUiThread(() -> binding.tvDownloadSpeed.setText(dec.format(downloadTest.getFinalDownloadRate())));
                            }
                        } else {
                            double downloadRate = downloadTest.getInstantDownloadRate();
                            downloadRateList.add(downloadRate);
                            position = getPositionByRate(downloadRate);

                            activity.runOnUiThread(() -> {
                                binding.pointerSpeedometer.speedTo(position);
                                rotate = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                rotate.setInterpolator(new LinearInterpolator());
                                rotate.setDuration(100);
                                binding.ivSpeedIndicator.startAnimation(rotate);
                                binding.tvDownloadSpeed.setText(dec.format(downloadTest.getInstantDownloadRate()));
                            });
                            lastPosition = position;
                            activity.runOnUiThread(() -> {
                                XYSeries downloadSeries = new XYSeries("");
                                downloadSeries.setTitle("");
                                List<Double> tmpLs = new ArrayList<>(downloadRateList);
                                int count = 0;
                                for (Double val : tmpLs) {
                                    downloadSeries.add(count++, val);
                                }
                                XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                dataset.addSeries(downloadSeries);
                            });

                        }
                    }
                    if (downloadTestFinished) {
                        if (uploadTestFinished) {
                            if (uploadTest.getFinalUploadRate() == 0) {
                                System.out.println("Upload error...");
                            } else {
                                activity.runOnUiThread(() -> binding.tvUploadSpeed.setText(dec.format(uploadTest.getFinalUploadRate())));
                            }
                        } else {
                            double uploadRate = uploadTest.getInstantUploadRate();
                            uploadRateList.add(uploadRate);
                            position = getPositionByRate(uploadRate);
                            activity.runOnUiThread(() -> {
                                binding.pointerSpeedometer.speedTo(lastPosition);
                                rotate = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                rotate.setInterpolator(new LinearInterpolator());
                                rotate.setDuration(100);
                                binding.ivSpeedIndicator.startAnimation(rotate);
                                binding.tvUploadSpeed.setText(dec.format(uploadTest.getInstantUploadRate()));
                            });
                            lastPosition = position;
                            activity.runOnUiThread(() -> {
                                XYSeries uploadSeries = new XYSeries("");
                                uploadSeries.setTitle("");
                                int count = 0;
                                List<Double> tmpLs = new ArrayList<>(uploadRateList);
                                for (Double val : tmpLs) {
                                    if (count == 0) {
                                        val = 0.0;
                                    }
                                    uploadSeries.add(count++, val);
                                }
                                XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                dataset.addSeries(uploadSeries);
                            });
                        }
                    }
                    if (pingTestFinished && downloadTestFinished && uploadTest.isFinished()) {
//                        activity.runOnUiThread(() -> switchClicks(true));
                        activity.runOnUiThread(() -> binding.pointerSpeedometer.speedTo(0));
                        break;
                    }
                    if (pingTest.isFinished()) {

                        pingTestFinished = true;
                    }
                    if (downloadTest.isFinished()) {
                        downloadTestFinished = true;
                    }
                    if (uploadTest.isFinished()) {
                        uploadTestFinished = true;
                    }
                    if (pingTestStarted && !pingTestFinished) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                }
                activity.runOnUiThread(() -> {
                    binding.tvStartRestartTest.setEnabled(true);
                    binding.tvStartRestartTest.setTextSize(16);
                    binding.tvStartRestartTest.setText("Restart Test");
                });
            }
        }).start();

    }

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: " + inetAddress.getHostAddress() + "\n";
                    }
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e + "\n";
        }
        return ip;
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions

                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (Exception e) {
            Log.e("getLocation: ", e.getLocalizedMessage());
        }
    }

    private void checkLocationisEnabledOrNot() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean networkEnabled = false;
        boolean gpsEnabled = false;
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gpsEnabled && !networkEnabled) {
            new AlertDialog.Builder(activity).setTitle("Enable GPS Service").setCancelable(false).setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }).setNegativeButton("Cancel", null).show();
        }
    }

    public int getPositionByRate(double rate) {
        if (rate <= 1) {
            return (int) (rate * 30);
        } else if (rate <= 10) {
            return (int) (rate * 6) + 30;
        } else if (rate <= 30) {
            return (int) ((rate - 10) * 3) + 90;
        } else if (rate <= 50) {
            return (int) ((rate - 30) * 1.5) + 150;
        } else if (rate <= 100) {
            return (int) ((rate - 50) * 1.2) + 180;
        }
        return 0;
    }


}