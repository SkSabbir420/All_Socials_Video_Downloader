/*
 * *
 *  * Created by Syed Usama Ahmad on 2/27/23, 1:22 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/26/23, 11:10 PM
 *
 */

package com.erickruo.allinonevideodownloader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.erickruo.allinonevideodownloader.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;


public class ShowImagesArrayAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<File> imageList;
    private LayoutInflater inflater;

    public ShowImagesArrayAdapter(Context context, ArrayList<File> imageList) {
        this.context = context;
        this.imageList = imageList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = inflater.inflate(R.layout.images_array_layout, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView = (ImageView) itemView.findViewById(R.id.fullimage);

        // setting the image in the imageView
        // imageView.setImageResource(images[position]);
        Glide.with(context).load(imageList.get(position).getPath()).placeholder(R.drawable.ic_appicon_pro).into(imageView);

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }


    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }


}