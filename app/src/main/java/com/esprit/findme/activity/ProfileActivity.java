package com.esprit.findme.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.esprit.findme.R;
import com.esprit.findme.main.MainActivity;
import com.esprit.findme.utils.AppConfig;
import com.esprit.findme.utils.SessionManager;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText name, email, phone;
    private CircularImageView image;
    private ImageView icone;
    private SessionManager session;
    private Toolbar toolbar;
    private Button updateBtn, backBtn;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.secondary_toolbar);
        setSupportActionBar(toolbar);
        name = (EditText) findViewById(R.id.tv_edit_name);
        email = (EditText) findViewById(R.id.tv_edit_email);
        phone = (EditText) findViewById(R.id.tv_edit_phone);
        image = (CircularImageView) findViewById(R.id.iv_edit_image);
        icone = (ImageView) findViewById(R.id.noImage);
        updateBtn = (Button) findViewById(R.id.nextBtn);
        backBtn = (Button) findViewById(R.id.returnBtn);

        image.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        session = new SessionManager(this.getApplicationContext());
        name.setText(session.getUserName());
        email.setText(session.getUserEmail());
        phone.setText(session.getUserPhone());
        if (session.getUserPhoto()!= "")
        {
            Picasso.with(this).load(session.getUserPhoto()).into(image);
        }
        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (email.getText().toString().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z]+.[a-zA-Z]{2,4}$"))
                {

                }
                else
                {
                    email.setError("invalid Email");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == backBtn) {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        }
        if (view == image){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
        if (view==updateBtn){
            String na = name.getText().toString().trim();
            String em = email.getText().toString().trim();
            String ph = phone.getText().toString().trim();
            if (!na.isEmpty() && !em.isEmpty() && !ph.isEmpty() && filePath!=null)
            {uploadMultipart();}
            else {
                Toast.makeText(this,
                        "Please enter your details!", Toast.LENGTH_LONG)
                        .show();
            }

        }
    }
    public void uploadMultipart() {


        //getting the actual path of the image
        String path = getPath(filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, AppConfig.URL_EDIT_USER)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("id", String.valueOf(session.getUserId())) //Adding text parameter to the request
                    .addParameter("name", name.getText().toString()) //Adding text parameter to the request
                    .addParameter("email", email.getText().toString()) //Adding text parameter to the request
                    .addParameter("phone", phone.getText().toString()) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
}
