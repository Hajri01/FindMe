package com.esprit.findme.main;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.findme.activity.JoinCircleActivity;
import com.esprit.findme.activity.MapsActivity;
import com.esprit.findme.activity.PostActivity;
import com.esprit.findme.dao.CircleDAO;
import com.esprit.findme.dao.UserDao;
import com.esprit.findme.fragment.ChatFragment;
import com.esprit.findme.fragment.HomeFragment;
import com.esprit.findme.R;
import com.esprit.findme.fragment.FriendsFragment;
import com.esprit.findme.models.Circle;
import com.esprit.findme.models.User;
import com.esprit.findme.utils.AppConfig;
import com.esprit.findme.utils.SessionManager;
import com.esprit.findme.utils.ViewPagerAdapter;
import com.esprit.findme.activity.AddCircleActivity;
import com.esprit.findme.fragment.ProfileFragment;
import com.esprit.findme.activity.LoginActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by TIBH on 07/11/2016.
 */

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    FloatingActionButton fab_plus, fab_cirlcle, fab_invite, fab_join;
    Animation fabOpen, fabClose, fabClockWise, fabAntiClockWise;
    boolean menuIsOpen = false;
    private SessionManager session;
    private ArrayList<Circle> circles ;
    private CircleDAO circleDao;
    private Spinner spinner;
    String[] circlesArray;
    ArrayAdapter<String> adapter;
    private int[] tabIcons = {
            R.drawable.ic_menu_home,
            R.drawable.ic_menu_circle,
            R.drawable.ic_menu_friends,
            R.drawable.ic_menu_chat
    };
    private String[] tabTitle = {
           " Home",
            "Circles",
            "Friends",
            "Chat"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(tabTitle[0]);
        setSupportActionBar(toolbar);
        circles= new ArrayList<>();
        circleDao = new CircleDAO();
        spinner=(Spinner) findViewById(R.id.spinner);
        circlesArray=new String[]{};
        session = new SessionManager(getApplicationContext());




        //fab menu settings
        fab_plus = (FloatingActionButton) findViewById(R.id.fab_plus);
        fab_cirlcle = (FloatingActionButton) findViewById(R.id.fab_circle);
        fab_invite = (FloatingActionButton) findViewById(R.id.fab_invite);
        fab_join = (FloatingActionButton) findViewById(R.id.fab_join);


        //initialisation
        fab_cirlcle.hide();
        fab_join.hide();
        fab_invite.hide();
        AllFloatingButtonActions(0);
        toolbar.setTitle("Home");

        toolbar.setTitleTextColor(0xFFFFFFFF);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabClockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        fabAntiClockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                AllFloatingButtonActions(position);
                toolbar.setTitle(tabTitle[position]);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //circlesArray = circles.toArray(circlesArray);

        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, AppConfig.URL_GET_CIRCLES+"?user_id=" +session.getUserId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response ) {
                        if (response != null) {
                            circleDao.getAllCircle(circles,response);
                            circlesArray=new String[circles.size()];
                            for (int k=0;k<circles.size();k++)
                            {
                                circlesArray[k]=circles.get(k).getTitle();
                            }
                           adapter = new ArrayAdapter<String>
                                    (MainActivity.this,android.R.layout.simple_spinner_item, circlesArray);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                            spinner.setAdapter(adapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                                    ((TextView) adapterView.getChildAt(0)).setTextSize(18);


                                    session.setCircleCode(circles.get(i).getCode());
                                    session.setCircleId(circles.get(i).getId());






                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
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

        // session manager

        if (!session.isLoggedIn()) {
            logoutUser();
        }

    }



    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);


        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void animateFab(int position) {
        switch (position) {
            case 0:
                fab_plus.hide();
                fab_cirlcle.hide();
                fab_join.hide();
                fab_plus.show();
                break;
            case 1:
                fab_plus.hide();
                fab_cirlcle.hide();
                fab_join.hide();
                fab_plus.show();
                break;

            case 2:
                fab_plus.hide();
                fab_cirlcle.hide();
                fab_join.hide();
                fab_plus.show();
                break;
            default:
                fab_plus.hide();
                fab_cirlcle.hide();
                fab_join.hide();
                fab_invite.hide();
                break;
        }
    }

    private void AllFloatingButtonActions(int position) {
        switch (position) {
            case 0:
                fab_plus.setImageDrawable(getResources().getDrawable(R.drawable.ic_post, null));

                animateFab(position);
                fab_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, PostActivity.class);
                        startActivity(intent);
                    }
                });

                break;
            case 1:
                fab_plus.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_plus, null));

                animateFab(position);
                fab_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (menuIsOpen) {
                            fab_cirlcle.startAnimation(fabClose);
                            fab_invite.startAnimation(fabClose);
                            fab_join.startAnimation(fabClose);
                            fab_plus.startAnimation(fabAntiClockWise);
                            fab_cirlcle.setClickable(false);
                            fab_invite.setClickable(false);
                            menuIsOpen = false;
                        } else {
                            fab_cirlcle.startAnimation(fabOpen);
                            fab_invite.startAnimation(fabOpen);
                            fab_join.startAnimation(fabOpen);
                            fab_plus.startAnimation(fabClockWise);
                            fab_cirlcle.setClickable(true);
                            fab_invite.setClickable(true);
                            menuIsOpen = true;
                        }
                    }
                });
                fab_cirlcle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, AddCircleActivity.class);
                        startActivity(intent);


                    }
                });
                fab_invite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO 5 implicit intent including an app chooser
                        // Create the text message with a string
                        String title = getResources().getString(R.string.chooser_title);
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, title + session.getCircleCode());
                        sendIntent.setType("text/plain");

                        // Verify that the intent will resolve to an activity
                        if (sendIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(sendIntent);
                        }

                    }
                });
                fab_join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, JoinCircleActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case 2:
                animateFab(position);
                fab_plus.setImageDrawable(getResources().getDrawable(R.drawable.ic_map, null));
                fab_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(intent);
                    }
                });

            default:
                animateFab(position);
                break;
        }
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);

    }

    private void setupViewPager(ViewPager viewPager) {

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new HomeFragment());
        viewPagerAdapter.addFragments(new ProfileFragment());
        viewPagerAdapter.addFragments(new FriendsFragment());
        viewPagerAdapter.addFragments(new ChatFragment());
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    }
    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */


}