package com.esprit.findme.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.esprit.findme.main.MainActivity;
import com.esprit.findme.utils.AppConfig;
import com.esprit.findme.utils.AppController;
import com.esprit.findme.R;
import com.esprit.findme.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCircleActivity extends AppCompatActivity {
    private Button btnRegister;
    private EditText inputTitle;
    private EditText inputCode;
    private EditText inputDescription;
    private ProgressDialog pDialog;
    private  int circle_id=0;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_circle);

        inputTitle = (EditText) findViewById(R.id.title);
        inputCode = (EditText) findViewById(R.id.code);
        inputDescription = (EditText) findViewById(R.id.description);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // Session manager
        session = new SessionManager(getApplicationContext());

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String title = inputTitle.getText().toString().trim();
                //TODO 3 code must be unique and random
                String code = inputCode.getText().toString().trim();
                String description = inputDescription.getText().toString().trim();
                //TODO 2 input control
                if (!title.isEmpty() && !code.isEmpty()) {
                    registerCircle(title, description, code, session.getUserId());

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    private void registerCircle(final String title, final String description,
                                final String code, final int creator) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_AddCircle, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject circles = jObj.getJSONObject("circle");
                    circle_id = circles.getInt("id");
                    session.setCircleId(circle_id);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Circle successfully created !", Toast.LENGTH_LONG).show();
                        // Launch Mainactivity
                        Intent intent = new Intent(
                                AddCircleActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");

                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", title);
                params.put("description", description);
                params.put("code", code);
                params.put("creator", String.valueOf(creator));
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
