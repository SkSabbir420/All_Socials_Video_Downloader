/*
 * *
 *  * Created by Syed Usama Ahmad on 3/2/23, 7:47 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/2/23, 7:18 PM
 *
 */

package com.erickruo.allinonevideodownloader.fragments;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.erickruo.allinonevideodownloader.BuildConfig;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.adapters.AudioAdapter;
import com.erickruo.allinonevideodownloader.databinding.FragmentAudiogalleryBinding;
import com.erickruo.allinonevideodownloader.models.SongInfo;
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

public class GalleryAudiosFragment extends Fragment {


    private final Handler myHandler = new Handler();
    private final Handler myHandlerProgress = new Handler();
    AudioAdapter audioAdapter;
    MediaPlayer mediaPlayer;
    private ArrayList<SongInfo> _songs;
    private Runnable runnable;
    private FragmentAudiogalleryBinding binding;
    private Runnable progressrunnable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remember add this line
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentAudiogalleryBinding.inflate(inflater, container, false);


        _songs = new ArrayList<>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.addItemDecoration(dividerItemDecoration);

       loadAllData();

        progressrunnable = new Runnable() {
            @Override
            public void run() {

                Log.d("Runwa", "run: " + 1);
                if (mediaPlayer != null) {
                    binding.seekBar.post(() -> {
                        try {
                            binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());

                        } catch (Exception ignored) {

                        }
                    });
                }

                myHandlerProgress.postDelayed(progressrunnable, 1000);
            }
        };

        myHandlerProgress.postDelayed(progressrunnable, 1000);


        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {

                    mediaPlayer.seekTo(seekBar.getProgress());

                }
            }
        });
        return binding.getRoot();
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
        String location = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS) + Constants.directoryInstaShoryDirectorydownload_audio;

        File[] files = new File(location).listFiles();
        if (files != null && files.length > 1) {
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        }
        if (files != null) {
            for (File file : files) {

                if (file.getName().endsWith(".mp3") || file.getName().endsWith(".m4a") || file.getName().endsWith(".wav")) {
                    Uri vv = Uri.parse(file.getAbsolutePath());
                    SongInfo s = new SongInfo(file.getName(), file.getName(), vv.toString());
                    _songs.add(s);
                }
            }
//            setAdapterRecyclerView();

        } else {

            requireActivity().runOnUiThread(() -> binding.noresultfound.setVisibility(View.VISIBLE));
        }
    }

    void setAdapterRecyclerView() {

        audioAdapter = new AudioAdapter(requireActivity(), _songs,  path -> {
            if (path == null) {
                requireActivity().runOnUiThread(() -> {
                    iUtils.ShowToastError(requireActivity(), requireActivity().getString(R.string.error_occ)
                    );
                });
            } else {
                FilePathUtility.deleteAndroid10andABOVE(requireActivity(), path, "a");
            }


        });
        binding.recyclerView.setAdapter(audioAdapter);

        audioAdapter.setOnItemClickListener((b,stop,  view, obj, position) -> {
            try {

                String tag = b.getTag().toString();

                System.out.println("myselectedtagis = "+tag);

                if (tag.equals(getString(R.string.stop_btn))) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    b.setVisibility(View.GONE);
                    stop.setVisibility(View.VISIBLE);
                } else {


                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;

                    }

                    runnable = () -> {
                        try {
                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setDataSource(obj.getSongUrl());
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(mp -> {
                                mp.start();
                                binding.seekBar.setProgress(0);
                                binding.seekBar.setMax(mediaPlayer.getDuration());
                                Log.d("Prog", "run: " + mediaPlayer.getDuration());
                            });

                            b.setVisibility(View.GONE);
                            stop.setVisibility(View.VISIBLE);
                        } catch (Exception ignored) {
                        }
                    };
                    myHandler.postDelayed(runnable, 100);

                }
            } catch (Exception e) {

                b.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Prog", "run: DESTROY CALLED");

        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
//            mediaPlayer.release();
                mediaPlayer = null;
            }
            myHandler.removeCallbacks(runnable);
            myHandlerProgress.removeCallbacks(progressrunnable);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}