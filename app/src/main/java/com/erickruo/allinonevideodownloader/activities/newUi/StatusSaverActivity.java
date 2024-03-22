/*
 * *
 *  * Created by Syed Usama Ahmad on 3/2/23, 7:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/2/23, 6:02 PM
 *
 */

package com.erickruo.allinonevideodownloader.activities.newUi;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.statussaver.StatusSaverMainFragment;

import java.util.Objects;

public class StatusSaverActivity extends AppCompatActivity {
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_saver);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        String frags = getIntent().getStringExtra("frag");
        if (frags.equals("status")) {
            fragment = new StatusSaverMainFragment();

        } else {
            fragment = new InstaFbStatusFragment();

        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

    }

    public String getMyData() {
        return MainActivityNewUi.myString;
    }


    public void setmydata(String mysa) {
        MainActivityNewUi.myString = mysa;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}