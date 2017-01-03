package com.esprit.findme.services;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.esprit.findme.activity.MapsActivity;
import com.esprit.findme.dao.UserDao;
import com.esprit.findme.fragments.FriendsFragment;
import com.esprit.findme.utils.SessionManager;

/**
 * Created by TIBH on 03/01/2017.
 */

public class GpsService extends IntentService implements LocationListener {
    private LocationManager locationManager;
    private String provider;
    UserDao userDao;
    SessionManager session;
    Location location;

    public GpsService(String name) {
        super(name);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        userDao = new UserDao();
        session = new SessionManager(getApplicationContext());
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = locationManager.getLastKnownLocation(provider);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (location != null) {
            onLocationChanged(location);
        } else {

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        String MyPosition = location.getLatitude() + "," + location.getLongitude();
        userDao.updateUserPosition(session.getUserId(), MyPosition);


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
