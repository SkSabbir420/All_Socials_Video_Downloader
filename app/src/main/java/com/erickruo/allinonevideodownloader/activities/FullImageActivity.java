/*
 * *
 *  * Created by Syed Usama Ahmad on 3/17/23, 11:37 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/17/23, 10:26 PM
 *
 */

package com.erickruo.allinonevideodownloader.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.databinding.ActivityFullViewBinding;
import com.erickruo.allinonevideodownloader.utils.GlideApp;
import com.erickruo.allinonevideodownloader.utils.iUtils;


public class FullImageActivity extends AppCompatActivity {

    private ActivityFullViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityFullViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initViews();

        if (getIntent().getStringExtra("myimgfile") != null && getIntent().getStringExtra("isfbimage") != null) {
            String filepath = getIntent().getStringExtra("myimgfile");
            String is = getIntent().getStringExtra("isfbimage");
            if (is.equals("true")) {
                showImage(filepath);
            } else {
                GlideApp.with(this)
                        .load(filepath)
                        .placeholder(R.drawable.ic_appicon_pro)
                        .into(binding.imageviewFullimg);
            }
        } else if (getIntent().getStringExtra("myimgfile") != null) {
            String filepath = getIntent().getStringExtra("myimgfile");
            GlideApp.with(this)
                    .load(filepath)
                    .placeholder(R.drawable.ic_appicon_pro)
                    .into(binding.imageviewFullimg);

        }
    }


    public void showImage(String str) {
        GlideApp.with(this).asBitmap().load(str).placeholder(R.drawable.ic_appicon_pro).addListener(new RequestListener<>() {
            public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
                iUtils.ShowToast(FullImageActivity.this, "Cant get image");
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                FullImageActivity.this.runOnUiThread(() -> binding.imageviewFullimg.setImageBitmap(resource));
                return false;
            }


        }).submit();
    }

    public void initViews() {
        binding.imgclose.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
