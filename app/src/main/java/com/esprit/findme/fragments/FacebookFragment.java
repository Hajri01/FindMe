package com.esprit.findme.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esprit.findme.R;
import com.esprit.findme.dao.UserDao;
import com.esprit.findme.main.MainActivity;
import com.esprit.findme.utils.SessionManager;

/**
 * Created by TIBH on 14/11/2016.
 */

public class FacebookFragment extends Fragment {
    private Button btnRegister;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private UserDao userDao;
    private String name;
    private String email;
    private  String number="";
    private String photo;


    public FacebookFragment(){


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_register_facebook,container,false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputPassword = (EditText) view.findViewById(R.id.passwordF);
        inputConfirmPassword=(EditText) view.findViewById(R.id.confirmF);
        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        userDao=new UserDao(getActivity());
        Bundle bundle=this.getArguments();
        if (bundle!=null)
        {
            email=bundle.getString("email");
            photo=bundle.getString("profile_pic");
            name=bundle.getString("name");
        }

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());

        // SQLite database handler
        //db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }



        inputConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable editable) {
                String password = inputPassword.getText().toString();
                String confirm = inputConfirmPassword.getText().toString();
                if (confirm.equals(password)){

                }
                else{
                    inputConfirmPassword.setError("Not the same password");
                }

            }
        });


        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String password = inputPassword.getText().toString().trim();
                String confirm = inputConfirmPassword.getText().toString().trim();
                if (!confirm.isEmpty() && !password.isEmpty()) {
                    userDao.registerUser(name,email,password,number);
                    userDao.updateUserPhoto(email,photo);
                    getFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).addToBackStack(null).commit();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
