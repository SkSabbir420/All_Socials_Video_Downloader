/*
 * *
 *  * Created by Syed Usama Ahmad on 3/4/23, 8:29 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/4/23, 8:04 PM
 *
 */

package com.erickruo.allinonevideodownloader.extraFeatures;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.erickruo.allinonevideodownloader.BuildConfig;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.activities.CoinsConsumableItemsActivity;
import com.erickruo.allinonevideodownloader.activities.InstagramBulkDownloaderSearch;
import com.erickruo.allinonevideodownloader.activities.InstagramFollowersList;
import com.erickruo.allinonevideodownloader.databinding.FragmentExtraFeaturesBinding;
import com.erickruo.allinonevideodownloader.extraFeatures.videolivewallpaper.MainActivityLivewallpaper;
import com.erickruo.allinonevideodownloader.utils.AdsManager;
import com.erickruo.allinonevideodownloader.utils.Constants;
import com.erickruo.allinonevideodownloader.utils.PrefsCoins;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsMainApp;
import com.erickruo.allinonevideodownloader.utils.WorldTimeTask;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class ExtraFeaturesFragment extends Fragment {
    private String nn = "nnn";
    FragmentExtraFeaturesBinding binding;
    PrefsCoins prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remember add this line
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentExtraFeaturesBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @SuppressLint({"SetTextI18n", "StaticFieldLeak"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            if (isAdded()) {

                if (!requireActivity().isFinishing()) {
                    MobileAds.initialize(
                            requireActivity(),
                            initializationStatus -> AdsManager.loadInterstitialAd(requireActivity()));
                }

                if (!Constants.isNonPlayStoreApp)
                {
                    binding.bulkcardvisibility.setVisibility(View.GONE);
                    binding.cardInstafollowers.setVisibility(View.GONE);
                }
                prefs = new PrefsCoins(requireActivity());
                nn = new SharedPrefsMainApp(requireActivity()).getPREFERENCE_inappads();
                binding.coinsTxt.setText("You have " + prefs.getInt("coins", 0) + " coin(s)");
                binding.btnOne.setOnClickListener(v -> {
                    startActivity(new Intent(requireActivity(), MainActivityLivewallpaper.class));
                    showAdmobAds();

                });

                binding.instafollowers.setOnClickListener(v -> {
                    startActivity(new Intent(requireActivity(), InstagramFollowersList.class));
                });


                binding.instabulkcard.setOnClickListener(v -> {
                    startActivity(new Intent(requireActivity(), InstagramBulkDownloaderSearch.class));
                    showAdmobAds();

                });

                binding.btnGetCoins.setOnClickListener(v -> {
                    startActivity(new Intent(requireActivity(), CoinsConsumableItemsActivity.class));
                    showAdmobAds();

                });


                if (System.currentTimeMillis() < prefs.getLong("time", System.currentTimeMillis())) {
                    binding.removedAd24.setVisibility(View.GONE);
                    binding.removedAd48.setVisibility(View.GONE);
                } else {
                    binding.removedAd24.setVisibility(View.VISIBLE);
                    binding.removedAd48.setVisibility(View.VISIBLE);
                }

                binding.removedAd24.setOnClickListener(v -> {
                    if (iUtils.isNetworkConnected(requireActivity())) {
                        if (prefs.getInt("coins", 0) >= 100) {
                            new WorldTimeTask(requireActivity()) {
                                @Override
                                protected void onPostExecute(Date date) {
                                    if (date != null) {
                                        try {
                                            SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy HH:mm:ss z", Locale.US);
                                            formatter.setTimeZone(TimeZone.getDefault());
                                            String dddd = formatter.format(date);
                                            System.out.println("network time " + dddd);


                                            long timeInMillis = date.getTime() + (86400000);

                                            binding.removedAd24.setVisibility(View.GONE);
                                            binding.removedAd48.setVisibility(View.GONE);
                                            prefs.setInt("coins", prefs.getInt("coins", 0) - 100);
                                            new SharedPrefsMainApp(requireActivity()).setPREFERENCE_inappads("ppp");

                                            prefs.setLong("time", timeInMillis);
                                            prefs.setPremium(1);


                                            requireActivity().runOnUiThread(() -> {
                                                if (isAdded()) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                                                    builder.setTitle(R.string.removeads);
                                                    builder.setMessage(R.string.adsareremoved);

                                                    builder.setPositiveButton(R.string.cancel, (dialog, which) -> {
                                                        dialog.dismiss();

                                                    });
                                                    builder.show();
                                                }
                                            });


                                        } catch (Exception e) {
                                            iUtils.ShowToastError(requireActivity(), getString(R.string.error_occ));
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            }.execute();


                        } else {
                            iUtils.ShowToastError(requireActivity(), getString(R.string.notenoughtcoins));

                        }
                    } else {
                        iUtils.ShowToastError(requireActivity(), getString(R.string.checkinternet));

                    }
                });


                binding.removedAd48.setOnClickListener(v -> {
                    if (iUtils.isNetworkConnected(requireActivity())) {
                        if (prefs.getInt("coins", 0) >= 200) {

                            new WorldTimeTask(requireActivity()) {
                                @Override
                                protected void onPostExecute(Date date) {
                                    if (date != null) {
                                        try {
                                            SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy HH:mm:ss z", Locale.US);
                                            formatter.setTimeZone(TimeZone.getDefault());
                                            String dddd = formatter.format(date);
                                            System.out.println("network time " + dddd);


                                            long timeInMillis = date.getTime() + (86400000 * 2);

                                            binding.removedAd24.setVisibility(View.GONE);
                                            binding.removedAd48.setVisibility(View.GONE);
                                            prefs.setInt("coins", prefs.getInt("coins", 0) - 200);
                                            new SharedPrefsMainApp(requireActivity()).setPREFERENCE_inappads("ppp");

                                            prefs.setLong("time", timeInMillis);
                                            prefs.setPremium(1);


                                            requireActivity().runOnUiThread(() -> {
                                                if (isAdded()) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                                                    builder.setTitle(R.string.removeads);
                                                    builder.setMessage(R.string.adsareremoved);

                                                    builder.setPositiveButton(R.string.cancel, (dialog, which) -> {
                                                        dialog.dismiss();

                                                    });
                                                    builder.show();
                                                }
                                            });


                                        } catch (Exception e) {
                                            iUtils.ShowToastError(requireActivity(), getString(R.string.error_occ));
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            }.execute();

                        } else {
                            iUtils.ShowToastError(requireActivity(), getString(R.string.notenoughtcoins));

                        }
                    } else {
                        iUtils.ShowToastError(requireActivity(), getString(R.string.checkinternet));

                    }
                });


                if (BuildConfig.ISPRO) {
                    binding.removedAd24.setEnabled(false);
                    binding.removedAd48.setEnabled(false);
                    binding.btnGetCoins.setEnabled(false);
                }

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void showAdmobAds() {
        if (Constants.show_Ads && !BuildConfig.ISPRO) {
            if (nn.equals("nnn")) {

                AdsManager.showAdmobInterstitialAd(requireActivity(), new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        AdsManager.loadInterstitialAd(requireActivity());

                    }
                });

            }
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        if (isAdded()) {
            binding.coinsTxt.setText(getString(R.string.available_coins_00) + prefs.getInt("coins", 0));
        }
    }

}
