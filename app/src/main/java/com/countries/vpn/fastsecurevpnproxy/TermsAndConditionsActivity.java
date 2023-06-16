package com.countries.vpn.fastsecurevpnproxy;

import android.os.Bundle;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.countries.vpn.fastsecurevpnproxy.databinding.ActivityTermsAndConditionsBinding;

public class TermsAndConditionsActivity extends AppCompatActivity {
    ActivityTermsAndConditionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_and_conditions);

        binding.wvTerms.loadUrl("");
        binding.wvTerms.setWebViewClient(new WebViewClient());
        binding.wvTerms.getSettings().setJavaScriptEnabled(true);
        binding.wvTerms.setWebViewClient(new WebViewClient());
        binding.wvTerms.getSettings().setLoadWithOverviewMode(true);
        binding.wvTerms.getSettings().setUseWideViewPort(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}