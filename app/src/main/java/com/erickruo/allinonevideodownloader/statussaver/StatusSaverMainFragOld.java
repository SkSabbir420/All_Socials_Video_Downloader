/*
 * *
 *  * Created by Syed Usama Ahmad on 2/27/23, 1:22 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/26/23, 11:10 PM
 *
 */

package com.erickruo.allinonevideodownloader.statussaver;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.adapters.WhatsappStoryAdapter;
import com.erickruo.allinonevideodownloader.models.WAStoryModel;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class StatusSaverMainFragOld extends Fragment {


    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;


    ArrayList<Object> filesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout recyclerLayout;
    private TextView grantpermissionand11;
    private TextView noresultfound;
    private TextView grantpermissionand11business;
    private LinearLayout grantlayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remember add this line
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_saver_main, container, false);

        if (isAdded() && !requireActivity().isFinishing()) {
            noresultfound = view.findViewById(R.id.noresultfound);
            recyclerView = view.findViewById(R.id.recycler_view);
            recyclerLayout = view.findViewById(R.id.swipeRecyclerViewlayout);
            grantpermissionand11 = view.findViewById(R.id.grantpermissionand11);
            grantpermissionand11business = view.findViewById(R.id.grantpermissionand11business);
            grantlayout = view.findViewById(R.id.grantlayout);
            grantlayout.setVisibility(View.GONE);
            recyclerLayout.setOnRefreshListener(() -> {
                try {


                    recyclerLayout.setRefreshing(true);
                    new MyTask(requireActivity()).execute();
                    (new Handler()).postDelayed(() -> {
                        recyclerLayout.setRefreshing(false);
                        requireActivity().runOnUiThread(() -> {
                            iUtils.ShowToast(requireActivity(), requireActivity().getString(R.string.refre)
                            );
                        });
                    }, 2000);
                } catch (Exception e) {
                    requireActivity().runOnUiThread(() -> {
                        iUtils.ShowToastError(requireActivity(), "Error in swipe refresh " + e.getMessage()
                        );
                    });
                }
            });
            if (isAdded()) {
                boolean result = checkPermission();
                if (result) {
                    new MyTask(requireActivity()).execute();

                }
            }
        }
        return view;
    }


    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (isAdded()) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(requireActivity());
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle(R.string.pernecessory);
                        alertBuilder.setMessage(R.string.write_neesory);
                        alertBuilder.setPositiveButton(R.string.yes, (dialog, which) -> ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE));
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    }
                } else {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void checkAgain() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (isAdded()) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(requireActivity());
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(R.string.pernecessory);
                alertBuilder.setMessage(R.string.write_neesory);
                alertBuilder.setPositiveButton(R.string.yes, (dialog, which) -> ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE));
                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new MyTask(requireActivity()).execute();
            } else {
                checkAgain();
            }
        }
    }

//    @SuppressLint("NotifyDataSetChanged")
//    private void setUpRecyclerView() {
//
//        try {
//
//            recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
//            WhatsappStoryAdapter recyclerViewAdapter = new WhatsappStoryAdapter(requireActivity(), getData());
//            recyclerView.setAdapter(recyclerViewAdapter);
//            recyclerViewAdapter.notifyDataSetChanged();
//
//        } catch (Throwable e) {
//            requireActivity().runOnUiThread(() -> {
//                iUtils.ShowToastError(requireActivity(), "Error Loading Data " + e.getMessage()
//                );
//            });
//            e.printStackTrace();
//        }
//    }


    private class MyTask extends AsyncTask<Void, Void, Void> {

        private Activity activity;

        public MyTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                WhatsappStoryAdapter recyclerViewAdapter = new WhatsappStoryAdapter(this.activity, getData());
                this.activity.runOnUiThread(() -> {
                    recyclerView.setLayoutManager(new GridLayoutManager(this.activity, 2));
                    recyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.notifyDataSetChanged();
                });


            } catch (Throwable e) {
                this.activity.runOnUiThread(() -> {
                    iUtils.ShowToastError(this.activity, "Error Loading Data " + e.getMessage()
                    );
                });
                e.printStackTrace();
            }
            return null;
        }
    }


    private ArrayList<Object> getData() {

        try {
            File[] fileslisttemp = getWhatsupFolder().listFiles();
            File[] fileslisttemp2 = getWhatsupBusinessFolder().listFiles();
            File[] files = ArrayUtils.addAll(fileslisttemp, fileslisttemp2);

            if (files != null) {
                Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);

                for (File file : files) {
                    WAStoryModel f = new WAStoryModel();
                    f.setName(getString(R.string.stor_saver));
                    f.setUri(Uri.fromFile(file));
                    f.setPath(file.getAbsolutePath());
                    f.setFilename(file.getName());

                    if (!file.getName().equals(".nomedia") && !file.getPath().equals("")) {
                        filesList.add(f);
                    }
                }

            } else {
                requireActivity().runOnUiThread(() -> noresultfound.setVisibility(View.VISIBLE));

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }


        return filesList;

    }


    public File getWhatsupFolder() {
        if (new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + ".Statuses").isDirectory()) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + ".Statuses");
        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + ".Statuses");
        }
    }

    public File getWhatsupBusinessFolder() {
        if (new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp.w4b/WhatsApp Business" + File.separator + "Media" + File.separator + ".Statuses").isDirectory()) {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp.w4b/WhatsApp Business" + File.separator + "Media" + File.separator + ".Statuses");
        } else {
            return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp Business" + File.separator + "Media" + File.separator + ".Statuses");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1239) {
            new MyTask(requireActivity()).execute();

        }

    }
}
