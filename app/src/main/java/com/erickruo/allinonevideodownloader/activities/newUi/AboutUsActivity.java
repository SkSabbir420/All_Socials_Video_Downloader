/*
 * *
 *  * Created by Syed Usama Ahmad on 12/19/22, 4:35 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 12/19/22, 3:39 PM
 *
 */

package com.erickruo.allinonevideodownloader.activities.newUi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.erickruo.allinonevideodownloader.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends AppCompatActivity {

    private ActivityAboutUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.tt.toolbar);

        binding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }
}