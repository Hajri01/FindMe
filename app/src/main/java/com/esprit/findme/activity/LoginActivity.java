package com.esprit.findme.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esprit.findme.R;
import com.esprit.findme.fragments.LoginFragment;


/**
 * Created by TIBH on 07/11/2016.
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getFragmentManager().beginTransaction().add(R.id.container,new LoginFragment()).commit();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

}
