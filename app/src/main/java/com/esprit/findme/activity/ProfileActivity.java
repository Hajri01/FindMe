package com.esprit.findme.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.esprit.findme.R;

public class ProfileActivity extends AppCompatActivity {
EditText name,email,phone;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (EditText) findViewById(R.id.tv_edit_name);
        email = (EditText) findViewById(R.id.tv_edit_email);


    }
}
