/*
 * *
 *  * Created by Syed Usama Ahmad on 2/27/23, 1:22 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/26/23, 11:10 PM
 *
 */

package com.erickruo.allinonevideodownloader.activities;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.erickruo.allinonevideodownloader.adapters.ShowImagesArrayAdapter;
import com.erickruo.allinonevideodownloader.databinding.ActivityFullArrayViewBinding;

import java.io.File;
import java.util.ArrayList;

public class FullViewArrayActivity extends AppCompatActivity {
    ActivityFullArrayViewBinding binding;
    private ArrayList<File> fileArrayList;
    private int position = 0;
    ShowImagesArrayAdapter showImagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullArrayViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fileArrayList = (ArrayList<File>) getIntent().getSerializableExtra("imagesList");

            position = getIntent().getIntExtra("pos", 0);
        }

        showImagesAdapter = new ShowImagesArrayAdapter(this, fileArrayList);
        binding.viepager.setAdapter(showImagesAdapter);
        binding.viepager.setCurrentItem(position);

        binding.viepager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                position = arg0;
                System.out.println("Current position==" + position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int num) {
            }
        });

        binding.close.setOnClickListener(v -> {
            onBackPressed();
        });


        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.viepager.getCurrentItem() <= fileArrayList.size()) {
                    binding.viepager.setCurrentItem(binding.viepager.getCurrentItem() + 1);
                }
            }
        });

        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.viepager.getCurrentItem() >= 0) {
                    binding.viepager.setCurrentItem(binding.viepager.getCurrentItem() - 1);
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
