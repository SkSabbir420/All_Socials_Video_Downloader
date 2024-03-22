/*
 * *
 *  * Created by Syed Usama Ahmad on 3/2/23, 7:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/2/23, 5:58 PM
 *
 */

package com.erickruo.allinonevideodownloader.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.erickruo.allinonevideodownloader.BuildConfig;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.adapters.GalleryAdapter;
import com.erickruo.allinonevideodownloader.databinding.FragmentStatusSaverGalleryBinding;
import com.erickruo.allinonevideodownloader.models.StatusSaverGalleryModel;
import com.erickruo.allinonevideodownloader.utils.Constants;
import com.erickruo.allinonevideodownloader.utils.FilePathUtility;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;


public class GalleryStatusSaver extends Fragment {

    public ArrayList<StatusSaverGalleryModel> statusSaverGalleryModelArrayList;
    AsyncTask<Void, Void, Void> fetchRecordingsAsyncTask;
    private FragmentStatusSaverGalleryBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remember add this line
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentStatusSaverGalleryBinding.inflate(inflater, container, false);
        statusSaverGalleryModelArrayList = new ArrayList<>();
        initViews();

//        fetchRecordingsAsyncTask = new FetchRecordingsAsyncTask(getActivity());
//        fetchRecordingsAsyncTask.execute();


     loadAllData();

        return binding.getRoot();
    }


    private void initViews() {


        binding.swiperefreshlayout.setOnRefreshListener(() -> {
          loadAllData();
            binding.swiperefreshlayout.setRefreshing(false);
        });
    }

    private void loadAllData() {
        try {
            Completable.fromAction(this::getAllFiles)
                    .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()) // Switch to the main thread
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            setAdapterRecyclerView();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (BuildConfig.DEBUG) {
                                Log.e("configureRxJavaErrorHandler", "failed to initialize youtubedl-android", e);
                            }
                            // Handle the error or display an error message here
                        }
                    });
        } catch (Exception e) {
            requireActivity().runOnUiThread(() -> {
                iUtils.ShowToastError(requireActivity(), requireActivity().getString(R.string.error_occ)
                );
            });
            e.printStackTrace();
        }
    }

    private void getAllFiles() {
        try {
            statusSaverGalleryModelArrayList = new ArrayList<>();


            String location = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SAVE_FOLDER_NAME;

            File[] files = new File(location).listFiles();


            if (files != null && files.length > 1) {
                Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            }
            if (files != null) {
                for (int i = 0; i < files.length; i++) {

                    statusSaverGalleryModelArrayList.add(new StatusSaverGalleryModel(getString(R.string.savedstt) + i, Uri.fromFile(files[i]), files[i].getAbsolutePath(), files[i].getName()));
                }

//                setAdapterRecyclerView();
            } else {
                requireActivity().runOnUiThread(() -> binding.noresultfound.setVisibility(View.VISIBLE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void setAdapterRecyclerView() {

        GalleryAdapter fileListAdapter = new GalleryAdapter(getActivity(), statusSaverGalleryModelArrayList, path -> {
            if (path == null) {
                requireActivity().runOnUiThread(() -> {
                    iUtils.ShowToastError(requireActivity(), requireActivity().getString(R.string.error_occ)
                    );
                });
            } else {
                FilePathUtility.deleteAndroid10andABOVE(requireActivity(), path, "v");

            }
        });
        binding.recststuslist.setAdapter(fileListAdapter);

    }


    private class FetchRecordingsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog;

        public FetchRecordingsAsyncTask(Context activity) {
            dialog = new ProgressDialog(activity, R.style.AppTheme_Dark_Dialog);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getString(R.string.loadingdata));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... args) {
            getAllFiles();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            setAdapterRecyclerView();
            // do UI work here
            if (dialog.isShowing()) {
                dialog.dismiss();
                if (fetchRecordingsAsyncTask != null) {
                    fetchRecordingsAsyncTask.cancel(true);
                }
            }
        }


    }

}