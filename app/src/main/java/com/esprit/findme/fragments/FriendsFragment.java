package com.esprit.findme.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.findme.R;
import com.esprit.findme.adapters.FriendsAdapter;
import com.esprit.findme.dao.UserDao;
import com.esprit.findme.main.MainActivity;
import com.esprit.findme.models.User;
import com.esprit.findme.services.RefreshService;
import com.esprit.findme.utils.AppConfig;
import com.esprit.findme.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TIBH on 23/11/2016.
 */

public class FriendsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FriendsAdapter adapter;
    private List<User> friendList;
    private UserDao userDao;
    SessionManager session;
    public IntentFilter mIntentFilter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final String mBroadcasAction = "com.esprit.findme.broadcast.string";
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getUserS();
            adapter.notifyDataSetChanged();
            Intent stopIntent = new Intent(getActivity(), RefreshService.class);
            getActivity().stopService(stopIntent);
        }
    };


    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mReceiver);
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutFriends);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcasAction);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        session = new SessionManager(getActivity().getApplicationContext());
        friendList = new ArrayList<>();
        userDao=new UserDao(getActivity());
        adapter = new FriendsAdapter(getActivity(), friendList,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        getUserS();
        return view;
    }
    private void refresh() {

        mSwipeRefreshLayout.setRefreshing(true);
        getUserS();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void getUserS() {
        friendList.clear();
        Volley.newRequestQueue(getActivity()).add(new StringRequest(Request.Method.GET, AppConfig.URL_GET_USERS + "?circle_id="
                +session.getCircleId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            userDao.getUsers(friendList, response);
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