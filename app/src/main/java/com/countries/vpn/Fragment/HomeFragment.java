package com.countries.vpn.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.countries.vpn.fastsecurevpnproxy.R;
import com.countries.vpn.fastsecurevpnproxy.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    FragmentHomeBinding binding;
    ActionBarDrawerToggle toggle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        toggle = new ActionBarDrawerToggle(requireActivity(), binding.drawer, R.string.opendrawer, R.string.closedrawer);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.ivBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawer.close();
            }
        });
        binding.ivVpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawer.close();
            }
        });

        binding.btnPower.setOnClickListener(view -> {
            if (binding.motionLayout.getProgress() == 0.0) {
                binding.motionLayout.transitionToEnd();
            } else {
                binding.motionLayout.transitionToStart();
            }
        });

        binding.motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {
                Log.e(TAG, "onTransitionStarted: ");
//                binding.imgTop.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {
//                Log.e(TAG, "onTransitionChange: " +i+"  ->  "+i1+" -> "+v);

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                Log.e(TAG, "onTransitionCompleted: -->  " + i);
//                binding.imgTop.setVisibility(View.VISIBLE);
            }


            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {
                Log.e(TAG, "onTransitionTrigger: ");
            }
        });

    }
}