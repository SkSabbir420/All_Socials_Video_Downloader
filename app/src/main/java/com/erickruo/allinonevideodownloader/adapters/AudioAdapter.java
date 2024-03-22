/*
 * *
 *  * Created by Syed Usama Ahmad on 2/27/23, 1:22 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/26/23, 11:10 PM
 *
 */

package com.erickruo.allinonevideodownloader.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.interfaces.OnClickFileDeleteListner;
import com.erickruo.allinonevideodownloader.models.SongInfo;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.SongHolder> {

    private final ArrayList<SongInfo> _songs;
    private final Activity context;
    private OnItemClickListener mOnItemClickListener;

    OnClickFileDeleteListner onClickFileDeleteListner;
    public AudioAdapter(Activity context, ArrayList<SongInfo> songs, OnClickFileDeleteListner onClickFileDeleteListner) {
        this.context = context;
        this._songs = songs;
        this.onClickFileDeleteListner = onClickFileDeleteListner;

    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myView = LayoutInflater.from(context).inflate(R.layout.row_songs, viewGroup, false);
        return new SongHolder(myView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final SongHolder songHolder, @SuppressLint("RecyclerView") final int i) {
        final SongInfo s = _songs.get(i);

        try {

            int completeLength = _songs.get(i).getSongname().length();
            int sizeoflast_ = StringUtils.ordinalIndexOf(_songs.get(i).getSongname(), "_", 3) + 1;
            songHolder.tvSongName.setText(_songs.get(i).getSongname().substring(sizeoflast_, completeLength));
            songHolder.tvSongArtist.setText(_songs.get(i).getSongname().substring(sizeoflast_, completeLength));

        } catch (Exception e) {
            songHolder.tvSongName.setText("no name");
            songHolder.tvSongArtist.setText("no name");

        }



        songHolder.btnAction.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(songHolder.btnAction, songHolder.btnActionStop, v, s, i);

            }
        });

        songHolder.btnActionStop.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(songHolder.btnActionStop, songHolder.btnAction, v, s, i);

            }
        });

        songHolder.mLayout.setOnLongClickListener(v -> {
            @SuppressLint("NotifyDataSetChanged") DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        try {

                            new File(s.getSongUrl()).delete();
                            _songs.remove(i);
                            onClickFileDeleteListner.delFile(new File(s.getSongUrl()).getAbsolutePath());
                            notifyDataSetChanged();
                            context.runOnUiThread(() -> {
                                iUtils.ShowToast(context, context.getString(R.string.delete)
                                );
                            });

                        } catch (Exception e) {
                            context.runOnUiThread(() -> {
                                iUtils.ShowToastError(context, "Error deleting"
                                );
                            });
                        }

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        dialog.dismiss();
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getString(R.string.are_sure_del)).setPositiveButton(context.getString(R.string.yes), dialogClickListener)
                    .setNegativeButton(context.getString(R.string.btn_no), dialogClickListener).show();

            return false;
        });

    }

    @Override
    public int getItemCount() {
        return _songs.size();
    }

    public interface OnItemClickListener {
        void onItemClick(ImageView play, ImageView stop, View view, SongInfo obj, int position);
    }

    public static class SongHolder extends RecyclerView.ViewHolder {
        TextView tvSongName, tvSongArtist;
        ImageView btnAction;
        LinearLayout mLayout;
        ImageView deleteSong;
        ImageView btnActionStop;

        public SongHolder(View itemView) {
            super(itemView);
            tvSongName = itemView.findViewById(R.id.tvSongName);
            deleteSong = itemView.findViewById(R.id.delte_song);
            tvSongArtist = itemView.findViewById(R.id.tvArtistName);
            btnAction = itemView.findViewById(R.id.btnPlay);
            btnActionStop = itemView.findViewById(R.id.btnPause);
            mLayout = itemView.findViewById(R.id.mLayout);
        }
    }
}