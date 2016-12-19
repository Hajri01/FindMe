package com.esprit.findme.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.findme.R;
import com.esprit.findme.activity.PostDetailsActivity;
import com.esprit.findme.models.News;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Med-Amine on 13/12/2016.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    private Context mContext;
    private List<News> newsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView writer;
        public TextView idNews;
        public TextView description,created_at;
        public ImageView profileImage, menuPost;

        public MyViewHolder(View view) {
            super(view);
            idNews = (TextView) view.findViewById(R.id.tvid_news_your_post);
            image = (ImageView) view.findViewById(R.id.news_image_your_post);
            writer = (TextView) view.findViewById(R.id.writer_your_post);
            description = (TextView) view.findViewById(R.id.content_your_post);
            profileImage = (ImageView) view.findViewById(R.id.tvUserImage_your_post);
            menuPost = (ImageView) view.findViewById(R.id.menuPost);
            created_at = (TextView) view.findViewById(R.id.tvTimee_your_post);
        }
    }

    public ProfileAdapter(Context mContext, List<News> newsList) {
        this.mContext = mContext;
        this.newsList = newsList;
    }

    @Override
    public ProfileAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.your_posts_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final News news = newsList.get(position);
        holder.idNews.setText(String.valueOf(news.getId()));
        Picasso.with(mContext).load(news.getUrl()).into(holder.image);
        holder.writer.setText(news.getUser_name());
        holder.description.setText(news.getDescription());
        Picasso.with(mContext).load(news.getPhoto()).into(holder.profileImage);
        holder.created_at.setText(news.getCreated_at());
        holder.menuPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.menuPost);
            }
        });

        // holder.tvDate.setText(new SimpleDateFormat("yyyy MMM dd").format(card.getDate()));

       /* holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PostDetailsActivity.class);
                intent.putExtra("news", news);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });*/
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_your_posts, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit_name:
//TODO edit post
                    return true;
                case R.id.action_delete_post:
//TODO delete post
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

}
