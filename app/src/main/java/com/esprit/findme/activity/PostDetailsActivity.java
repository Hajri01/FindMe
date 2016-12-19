package com.esprit.findme.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.findme.R;
import com.esprit.findme.adapter.ImageAdapter;
import com.esprit.findme.dao.ImageDao;
import com.esprit.findme.models.Image;
import com.esprit.findme.models.News;
import com.esprit.findme.utils.AppConfig;

import java.util.ArrayList;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {
    private RecyclerView imageRecyclerView;
    private ImageAdapter adapter;
    private List<Image> imageList;
    private ImageDao imageDao;
    private TextView writer;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        writer = (TextView) findViewById(R.id.tvwriter);
        description = (TextView) findViewById(R.id.tvdescription);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageRecyclerView = (RecyclerView) findViewById(R.id.images_recycler_view);
        imageList = new ArrayList<>();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        imageDao = new ImageDao();
        adapter = new ImageAdapter(this, imageList);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        // RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        imageRecyclerView.setLayoutManager(layoutManager);
        imageRecyclerView.setAdapter(adapter);
        if (getIntent().getSerializableExtra("news") != null) {
            News news = (News) getIntent().getSerializableExtra("news");
            getImages(news.getId());
            writer.setText(news.getUser_name());
            description.setText(news.getDescription());
        }

    }
    private void getImages(int idNews){
        imageList.clear();

        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, AppConfig.URL_GET_IMAGES_BY_NEWS_ID+"?news_id="+idNews,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response ) {
                        if (response != null) {
                            imageDao.getImages(imageList,response);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error setting note : " + error.getMessage());
                if (error instanceof TimeoutError) {
                    System.out.println("erreur");
                }
            }
        }));
    }
}
