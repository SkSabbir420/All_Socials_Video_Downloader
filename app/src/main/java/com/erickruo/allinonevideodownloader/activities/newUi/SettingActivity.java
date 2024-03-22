/*
 * *
 *  * Created by Syed Usama Ahmad on 3/3/23, 12:53 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/2/23, 11:12 PM
 *
 */

package com.erickruo.allinonevideodownloader.activities.newUi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.databinding.ActivitySettingBinding;
import com.erickruo.allinonevideodownloader.models.adminpanel.Setting;
import com.erickruo.allinonevideodownloader.utils.Constants;
import com.erickruo.allinonevideodownloader.utils.iUtils;


public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.tt1.toolbar);


        binding.moreapps.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=" + getString(R.string.google_developer_id))));
            } catch (android.content.ActivityNotFoundException anfe) {

                runOnUiThread(() -> {
                    iUtils.ShowToastError(SettingActivity.this, getString(R.string.error_occ)
                    );
                });

            }

        });

        binding.ratetheapp.setOnClickListener(v -> {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }

        });


        binding.shareapp.setOnClickListener(v -> {
            try {
                iUtils.shareApp(SettingActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }


        });

        binding.mailussuggsions.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"" + Setting.getEmail(SettingActivity.this, Constants.iSAdminAttached)});
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.suggs));
            i.putExtra(Intent.EXTRA_TEXT, getString(R.string.entermesss));
            try {
                startActivity(Intent.createChooser(i, " "));
            } catch (android.content.ActivityNotFoundException ex) {
                runOnUiThread(() -> {
                    iUtils.ShowToastError(SettingActivity.this, getString(R.string.error_occ)
                    );
                });
            }
        });

        binding.aboutuspage.setOnClickListener(v -> startActivity(new Intent(SettingActivity.this, AboutUsActivity.class)));
        binding.privacypolicy.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("" + Setting.getPrivacyPolicy(SettingActivity.this,
                        Constants.iSAdminAttached))));
            } catch (android.content.ActivityNotFoundException anfe) {
                runOnUiThread(() -> {
                    iUtils.ShowToastError(SettingActivity.this, getString(R.string.error_occ)
                    );
                });
            }
        });


    }


}