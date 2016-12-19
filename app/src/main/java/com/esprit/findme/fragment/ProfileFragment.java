package com.esprit.findme.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.findme.adapter.NewsAdapter;
import com.esprit.findme.adapter.ProfileAdapter;
import com.esprit.findme.dao.NewsDao;
import com.esprit.findme.models.News;
import com.esprit.findme.utils.AppConfig;
import com.esprit.findme.adapter.CirclesAdapter;
import com.esprit.findme.R;

import com.esprit.findme.models.Circle;
import com.esprit.findme.dao.CircleDAO;
import com.esprit.findme.utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class ProfileFragment extends Fragment {
    private ImageView profilePicture;
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private List<News> newsList;
    NewsDao newsDao;
    SessionManager session;




    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        session = new SessionManager(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rc_your_news);
        profilePicture = (ImageView) view.findViewById(R.id.iv_profile);
        Picasso.with(getActivity()).load(session.getUserPhoto()).into(profilePicture);

        newsList = new ArrayList<>();
        newsDao = new NewsDao();
        adapter = new ProfileAdapter(getActivity(), newsList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        getNews();
        Toast.makeText(getContext(),newsList.size()+"",Toast.LENGTH_LONG).show();


        return view;

    }
    private void getNews() {
        newsList.clear();

        Volley.newRequestQueue(getActivity()).add(new StringRequest(Request.Method.GET, AppConfig.URL_GET_NEWS_BY_USER+"?user_id="+session.getUserId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            newsDao.getAllNews(newsList, response);
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
