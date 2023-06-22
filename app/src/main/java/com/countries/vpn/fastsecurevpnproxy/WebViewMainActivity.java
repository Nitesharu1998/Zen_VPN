package com.countries.vpn.fastsecurevpnproxy;

import static com.countries.vpn.AdsUtils.Utils.Global.isArrayListNull;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.countries.vpn.Adapters.BookmarksAdapter;
import com.countries.vpn.Adapters.WebTabsAdapter;
import com.countries.vpn.AdsUtils.Interfaces.AppInterfaces;
import com.countries.vpn.AdsUtils.PreferencesManager.AppPreferences;
import com.countries.vpn.AdsUtils.Utils.Constants;
import com.countries.vpn.AdsUtils.Utils.Global;
import com.countries.vpn.CommonDataModels.WebLinksDataModel;
import com.countries.vpn.fastsecurevpnproxy.databinding.ActivityWebViewMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class WebViewMainActivity extends AppCompatActivity {
    String urlToHit;
    ActivityWebViewMainBinding binding;
    Activity activity;
    private boolean isSwitchTabActive = false;
    private ArrayList<String> normalTabs = new ArrayList<>();
    AppPreferences preferences;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_web_view_main);
        initListeners();
        Global.hideSoftKeyBoard(activity);
        preferences = new AppPreferences(activity);
        normalTabs = preferences.getStoredNormalTabs();
        if (getIntent().hasExtra(Constants.URL_INTENT)) {
            urlToHit = getIntent().getStringExtra(Constants.URL_INTENT);
            binding.edtWebsite.setText(urlToHit.trim().toLowerCase());
            if (!URLUtil.isValidUrl(urlToHit)) {
                Toast.makeText(activity, "Please Enter Valid URL", Toast.LENGTH_SHORT).show();
            } else
                Global.loadURLinWebView(binding.tvProgress,binding.wvMain, urlToHit, binding.ivBackPress, binding.ivNextPress, new AppInterfaces.WebViewInterface() {
                    @Override
                    public void getClickedURL(String URL) {
                        if (Constants.isPrivateTab) {
                            Constants.isPrivateTab = false;
                            if (!Constants.privateLinksArray.contains("")) {
                                Constants.privateLinksArray.add(0, "");
                            }
                            Constants.privateLinksArray.add(URL);
                        } else {
                            normalTabs.add(URL);
                            preferences.setStoredNormalTabs(normalTabs);
                        }

                        binding.edtWebsite.getText().clear();
                        binding.edtWebsite.setText(URL.trim());
                    }

                    @Override
                    public void getBitmap(Bitmap image) {
                    }
                });
        }

    }

    private void initListeners() {

        binding.ivSwitchTabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSwitchTabActive) {
                    binding.ivShowBookmarks.setBackgroundResource(R.drawable.tab_transparent);
                    binding.ivSwitchTabs.setBackgroundResource(R.drawable.tab_bg);
                    isSwitchTabActive = true;
                    showBookMarkUI(2);
                    setTabs();
                }
            }
        });

        binding.ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.tabSelector.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Constants.isPrivateTab = false;
                        binding.rlTabMainBar.setBackgroundResource(R.drawable.ic_navbg);
                        binding.llAddNewPrivateTab.setVisibility(View.GONE);
                        setNormalTabsList(normalTabs);
                        break;
                    case 1:
                        Constants.isPrivateTab = true;
                        binding.rlTabMainBar.setBackgroundResource(R.drawable.ic_private_bg);
                        setPrivateTabList(Constants.privateLinksArray);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.ivAddNewBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.openAddBookMarkDialog(activity, "");
            }
        });
        binding.ivAddBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.openAddBookMarkDialog(activity, binding.edtWebsite.getText().toString());
            }
        });
        binding.edtWebsite.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    Global.hideSoftKeyBoard(activity);
                    if (!URLUtil.isValidUrl(binding.edtWebsite.getText().toString())) {
                        Toast.makeText(activity, "Please Enter Valid URL", Toast.LENGTH_SHORT).show();
                    } else
                        Global.loadURLinWebView(binding.tvProgress, binding.wvMain, binding.edtWebsite.getText().toString(), binding.ivBackPress, binding.ivNextPress, new AppInterfaces.WebViewInterface() {
                            @Override
                            public void getClickedURL(String URL) {
                                normalTabs.add(URL);
                                preferences.setStoredNormalTabs(normalTabs);
                                binding.edtWebsite.getText().clear();
                                binding.edtWebsite.setText(URL.trim());
                                isSwitchTabActive = false;
                            }

                            @Override
                            public void getBitmap(Bitmap image) {
                            }
                        });

                    return true;
                }
                return false;
            }
        });

        binding.ivShowBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSwitchTabActive = false;
                binding.ivShowBookmarks.setBackgroundResource(R.drawable.tab_bg);
                binding.ivSwitchTabs.setBackgroundResource(R.drawable.tab_transparent);
                showBookMarkUI(0);
                setUpList();
            }
        });
        binding.ivAddTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
    }

    private void setPrivateTabList(ArrayList<String> privateLinksArray) {
        binding.rclListTabs.removeAllViews();
        if (isArrayListNull(privateLinksArray)) {
            binding.rclListTabs.setVisibility(View.GONE);
            binding.llAddNewPrivateTab.setVisibility(View.VISIBLE);
        } else {
            GridLayoutManager manager = new GridLayoutManager(activity, 2);
            manager.setOrientation(RecyclerView.VERTICAL);
            binding.rclListTabs.setLayoutManager(manager);
            WebTabsAdapter adapter = new WebTabsAdapter(Constants.privateLinksArray, activity, new AppInterfaces.TabAdapterInterface() {
                @Override
                public void removeTab(int Position) {

                    Constants.privateLinksArray.remove(Position);
                    setPrivateTabList(Constants.privateLinksArray);
                }

                @Override
                public void hitLink(int Position) {
                    isSwitchTabActive = false;
                    showBookMarkUI(1);
                    Global.loadURLinWebView(binding.tvProgress, binding.wvMain, normalTabs.get(Position), binding.ivBackPress, binding.ivNextPress, new AppInterfaces.WebViewInterface() {
                        @Override
                        public void getClickedURL(String URL) {
                            binding.ivSwitchTabs.setBackgroundResource(R.drawable.tab_transparent);
                            binding.edtWebsite.getText().clear();
                            binding.edtWebsite.setText(URL.trim());
                        }

                        @Override
                        public void getBitmap(Bitmap image) {
                        }
                    });
                }

                @Override
                public void addNewTab() {
                    onBackPressed();
                }
            });
            binding.rclListTabs.setAdapter(adapter);
        }
    }

    private void setNormalTabsList(ArrayList<String> normalTabs) {
        GridLayoutManager manager = new GridLayoutManager(activity, 2);
        manager.setOrientation(RecyclerView.VERTICAL);
        if (!isArrayListNull(normalTabs) && !normalTabs.get(0).equals("")) {
            normalTabs.add(0, "");
        }
        binding.rclListTabs.setVisibility(View.VISIBLE);
        binding.rclListTabs.setLayoutManager(manager);
        WebTabsAdapter adapter = new WebTabsAdapter(normalTabs, activity, new AppInterfaces.TabAdapterInterface() {
            @Override
            public void removeTab(int position) {
                binding.ivSwitchTabs.setBackgroundResource(R.drawable.tab_transparent);
                normalTabs.remove(position);
                preferences.setStoredNormalTabs(normalTabs);
                setNormalTabsList(normalTabs);
            }

            @Override
            public void hitLink(int position) {
                binding.ivSwitchTabs.setBackgroundResource(R.drawable.tab_transparent);
                isSwitchTabActive = false;
                showBookMarkUI(1);
                Global.loadURLinWebView(binding.tvProgress, binding.wvMain, normalTabs.get(position), binding.ivBackPress, binding.ivNextPress, new AppInterfaces.WebViewInterface() {
                    @Override
                    public void getClickedURL(String URL) {
                        binding.edtWebsite.getText().clear();
                        binding.edtWebsite.setText(URL.trim());
                    }

                    @Override
                    public void getBitmap(Bitmap image) {
                    }
                });
            }

            @Override
            public void addNewTab() {
                onBackPressed();
            }
        });
        binding.rclListTabs.setAdapter(adapter);

    }

    private void setTabs() {
        binding.tabSelector.removeAllTabs();
        binding.tabSelector.addTab(binding.tabSelector.newTab().setText("Normal")/*.setIcon(R.drawable.ic_normal_tab)*/);
        binding.tabSelector.addTab(binding.tabSelector.newTab().setText("Private")/*.setIcon(R.drawable.ic_private_tab)*/);
    }

    private void setUpList() {
        ArrayList<WebLinksDataModel> models = Global.getWebLinkDataModel(activity);
        if (!models.isEmpty()) {
            LinearLayoutManager manager = new LinearLayoutManager(activity);
            manager.setOrientation(RecyclerView.VERTICAL);
            binding.rclListBookmarks.setLayoutManager(manager);
            BookmarksAdapter adapter = new BookmarksAdapter(activity, models, new AppInterfaces.AdapterClick() {
                @Override
                public void clickedPosition(int position) {
                    binding.ivShowBookmarks.setBackgroundResource(R.drawable.tab_transparent);
                    showBookMarkUI(1);
                    binding.edtWebsite.setText(models.get(position).getURL());
                    Global.loadURLinWebView(binding.tvProgress, binding.wvMain, models.get(position).getURL(), binding.ivBackPress, binding.ivNextPress, new AppInterfaces.WebViewInterface() {
                        @Override
                        public void getClickedURL(String URL) {
                            binding.edtWebsite.getText().clear();
                            binding.edtWebsite.setText(URL.trim());
                        }

                        @Override
                        public void getBitmap(Bitmap image) {
                        }
                    });
                }
            });
            binding.rclListBookmarks.setAdapter(adapter);
        } else {
            Toast.makeText(activity, "No Bookmarks are saved currently...", Toast.LENGTH_SHORT).show();
        }


    }

    private void showBookMarkUI(int shouldShow) {
        if (shouldShow == 0) {
            binding.llAppbar.setVisibility(View.GONE);
            binding.wvMain.setVisibility(View.GONE);
            binding.rclListBookmarks.setVisibility(View.VISIBLE);
            binding.rlBookmarkBar.setVisibility(View.VISIBLE);

            binding.rlTabMainBar.setVisibility(View.GONE);
            binding.tabSelector.setVisibility(View.GONE);
            binding.rclListTabs.setVisibility(View.GONE);

        } else if (shouldShow == 1) {
            binding.rlBookmarkBar.setVisibility(View.GONE);
            binding.rclListBookmarks.setVisibility(View.GONE);
            binding.wvMain.setVisibility(View.VISIBLE);
            binding.llAppbar.setVisibility(View.VISIBLE);

            binding.rlTabMainBar.setVisibility(View.GONE);
            binding.tabSelector.setVisibility(View.GONE);
            binding.rclListTabs.setVisibility(View.GONE);


        } else if (shouldShow == 2) {
            binding.rlBookmarkBar.setVisibility(View.GONE);
            binding.rclListBookmarks.setVisibility(View.GONE);
            binding.wvMain.setVisibility(View.GONE);
            binding.llAppbar.setVisibility(View.GONE);

            binding.rlTabMainBar.setVisibility(View.VISIBLE);
            binding.tabSelector.setVisibility(View.VISIBLE);
            binding.rclListTabs.setVisibility(View.VISIBLE);


        }
    }
}