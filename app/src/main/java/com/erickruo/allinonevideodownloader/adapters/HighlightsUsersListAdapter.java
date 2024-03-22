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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.interfaces.HighlightsListInStoryListner;
import com.erickruo.allinonevideodownloader.models.storymodels.ModelHighlightsUsrTray;
import com.erickruo.allinonevideodownloader.utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HighlightsUsersListAdapter extends RecyclerView.Adapter<HighlightsUsersListAdapter.ViewHolder> implements Filterable {
    HighlightsListInStoryListner userListInStoryListner;
    private final Activity context;
    private ArrayList<ModelHighlightsUsrTray> userListIninstaStory;
    private final ArrayList<ModelHighlightsUsrTray> userListIninstaStoryOrignal;


    public HighlightsUsersListAdapter(Activity context, ArrayList<ModelHighlightsUsrTray> userListIninstaStory, HighlightsListInStoryListner userListInStoryListner) {
        this.context = context;
        this.userListIninstaStory = userListIninstaStory;
        this.userListInStoryListner = userListInStoryListner;
        this.userListIninstaStoryOrignal = userListIninstaStory;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_userlist_placeholder, viewGroup, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        try {
            if (userListIninstaStory.get(position).getMedia_count() > 0) {

                viewHolder.rec_item_username.setVisibility(View.GONE);
                viewHolder.rec_user_fullname.setVisibility(View.GONE);


                try {
                    GlideApp.with(context).load(userListIninstaStory.get(position).getCoverMedia().getCroppedImageVersion().getURL() + "")
                            .thumbnail(0.2f).placeholder(R.drawable.ic_appicon_pro).into(viewHolder.story_item_imgview);
                } catch (Exception e) {
                    System.out.println("errorisnnnnnn: " + e.getMessage());

                    try {
                        GlideApp.with(context).load(userListIninstaStory.get(position).getUser().getProfile_pic_url())
                                .thumbnail(0.2f).placeholder(R.drawable.ic_appicon_pro).into(viewHolder.story_item_imgview);
                    } catch (Exception e1) {
                        System.out.println("errorisnnnnnn: " + e1.getMessage());
                    }
                }
                viewHolder.rec_item_relativelative_layot.setOnClickListener(view -> {

                    System.out.println("response1122ff334455:   workingggg");
                    userListInStoryListner.onclickUserHighlightsListItem(position, userListIninstaStory.get(position));


                });


            }

        } catch (Exception e) {
            System.out.println("errorisnnnnnn: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return userListIninstaStory == null ? 0 : userListIninstaStory.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressLint("NotifyDataSetChanged")
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userListIninstaStory = (ArrayList<ModelHighlightsUsrTray>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<ModelHighlightsUsrTray> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = userListIninstaStoryOrignal;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<ModelHighlightsUsrTray> getFilteredResults(String constraint) {
        List<ModelHighlightsUsrTray> results = new ArrayList<>();

        for (ModelHighlightsUsrTray item : userListIninstaStoryOrignal) {
            if (item.getUser().getUsername().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout rec_item_relativelative_layot;
        private final CircleImageView story_item_imgview;
        private final TextView rec_item_username;
        private final TextView rec_user_fullname;

        public ViewHolder(View view) {
            super(view);

            rec_item_relativelative_layot = view.findViewById(R.id.rec_item_relativelative_layot);
            story_item_imgview = view.findViewById(R.id.story_item_imgview);
            rec_item_username = view.findViewById(R.id.rec_item_username);
            rec_user_fullname = view.findViewById(R.id.rec_user_fullname);

        }
    }
}