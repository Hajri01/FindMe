package com.esprit.findme.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.esprit.findme.R;
import com.esprit.findme.models.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Med-Amine on 29/11/2016.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private Context mContext;
    private List<Image> imgList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image_item);
        }
    }

    public ImageAdapter(Context mContext, List<Image> imgList) {
        this.mContext = mContext;
        this.imgList = imgList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.MyViewHolder holder, int position) {
        Image img = imgList.get(position);
        Picasso.with(mContext).load(img.getUrl()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }
}
