package com.countries.vpn.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.countries.vpn.Adapters.BookmarksListAdapter;
import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.AdsUtils.Utils.Constants;
import com.countries.vpn.AdsUtils.Utils.Global;
import com.countries.vpn.CommonDataModels.WebLinksDataModel;
import com.countries.vpn.fastsecurevpnproxy.R;
import com.countries.vpn.fastsecurevpnproxy.WebViewMainActivity;
import com.countries.vpn.fastsecurevpnproxy.databinding.FragmentWebSurfingBinding;
import com.countries.vpn.fastsecurevpnproxy.databinding.LayoutBookmarksBinding;

import java.util.ArrayList;

public class WebSurfingFragment extends Fragment {
    FragmentWebSurfingBinding binding;
    String[] linkNames = {Constants.FACEBOOK, Constants.GOOGLE, Constants.INSTAGRAM, Constants.CRICINFO, Constants.STOCK, Constants.NEWS};
    String[] linkTitles = {"Facebook", "Google", "Instagram", "Live Updates", "Stocks", "Short News"};
    int[] linkImages = {R.drawable.ic_facebook, R.drawable.ic_google, R.drawable.ic_instagram, R.drawable.ic_liveupdate, R.drawable.ic_stock, R.drawable.ic_news};
    ArrayList<WebLinksDataModel> linkModel = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web_surfing, container, false);
        linkModel = Global.getWebLinkDataModel(requireContext());
        setUpStaticGrid();

        binding.edtWebsite.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    startActivity(new Intent(requireContext(), WebViewMainActivity.class).putExtra(Constants.URL_INTENT, binding.edtWebsite.getText().toString()));
                    binding.edtWebsite.getText().clear();
                    return true;
                }

                return false;
            }
        });

        binding.ivListSavedBookMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.isArrayListNull(linkModel)) {
                    Toast.makeText(requireActivity(), "No Bookmarks are saved", Toast.LENGTH_SHORT).show();
                } else {
                    showLinksDialog(requireContext(), linkModel);
                }
            }
        });

        return binding.getRoot();
    }

    private void showLinksDialog(Context context, ArrayList<WebLinksDataModel> linkModel) {
        Dialog dialog = new Dialog(context);
        LayoutBookmarksBinding bindingBookmark = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_bookmarks, null, false);
        dialog.setContentView(bindingBookmark.getRoot());
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.VERTICAL);
        bindingBookmark.rclBookmarkList.setLayoutManager(manager);
        BookmarksListAdapter adapter = new BookmarksListAdapter(context, linkModel, new AppInterfaces.AdapterClick() {
            @Override
            public void clickedPosition(int position) {
                dialog.dismiss();
                startActivity(new Intent(requireContext(), WebViewMainActivity.class).putExtra(Constants.URL_INTENT, linkModel.get(position).getURL()));
            }
        });
        bindingBookmark.rclBookmarkList.setAdapter(adapter);
        dialog.show();

    }

    private void setUpStaticGrid() {
        for (int i = 0; i < linkImages.length; i++) {
            int test = i;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 20, 30, 20);
            LinearLayout linearLayout = new LinearLayout(requireContext());
            ImageView imageView = new ImageView(requireContext());
            TextView textView = new TextView(requireContext());
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setId(ViewCompat.generateViewId());
            imageView.setLayoutParams(new ViewGroup.LayoutParams((int) getResources().getDimension(com.intuit.sdp.R.dimen._48sdp), (int) getResources().getDimension(com.intuit.sdp.R.dimen._48sdp)));
            imageView.setImageResource(linkImages[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setGravity(Gravity.CENTER);
            textView.setText("" + linkTitles[i]);

            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            binding.glStaticgrid.addView(linearLayout);
            binding.glStaticgrid.setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("onClick: ", "clicked link================>>>>>>>>>>" + linkNames[test]);
                    startActivity(new Intent(requireActivity(), WebViewMainActivity.class).putExtra(Constants.URL_INTENT, linkNames[test]));
                }
            });
        }
    }
}