package com.orinameh.googlemapdemo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.orinameh.googlemapdemo.R;
import com.orinameh.googlemapdemo.application.AppConfig;
import com.orinameh.googlemapdemo.application.ProjectApp;
import com.orinameh.googlemapdemo.helpers.SQLiteHandler;
import com.orinameh.googlemapdemo.helpers.SessionManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private static final int GALLERY_REQUEST = 1;
    private ImageButton mSelectImage;
    private Uri mImageUri = null;

    private EditText fname;
    private EditText lname;
    private EditText regEmail;
    private EditText regPass;
    private EditText regPassConf;
    private Button   btnCreate;

    private ProgressDialog dialog;
    private SessionManager sessionManager;
//    private SQLiteHandler db;

    private String firstName, lastName, inputEmail, inputPassword, inputPassConf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mSelectImage = (ImageButton) findViewById(R.id.image_button);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        fname = (EditText) findViewById(R.id.first_name);
        lname = (EditText) findViewById(R.id.last_name);
        regEmail = (EditText) findViewById(R.id.reg_email);
        regPass = (EditText) findViewById(R.id.reg_pass);
        regPassConf = (EditText) findViewById(R.id.confirm_password);
        btnCreate = (Button) findViewById(R.id.create_acct);

        //Progress Dialog
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        //Session Manager
        sessionManager = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (sessionManager.isUserLoggedIn()){
            // User is already logged in. Take him to main activity
            startMapsActivity();

        }

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = fname.getText().toString().trim();
                lastName = lname.getText().toString().trim();
                inputEmail = regEmail.getText().toString().trim();
                inputPassword = regPass.getText().toString().trim();
                inputPassConf = regPassConf.getText().toString().trim();


                if (!firstName.isEmpty() && !lastName.isEmpty() && !inputEmail.isEmpty() && !inputPassword.isEmpty() && !inputPassConf.isEmpty()){

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()){
                        regEmail.setError("Enter Valid Email Address");

                    }

                    if (!inputPassConf.contentEquals(inputPassConf)){

                        Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                    }

                    registerUser(firstName, lastName, inputEmail, inputPassword, inputPassConf);


                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

//    Function to store user parameters
    private void registerUser(final String firstName, final String lastName, final String email,
                              final String password, final String passConfirm){

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        dialog.setMessage("Registering....");
        showDialog();

        StringRequest registerRequest = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Register Response: " + response);
                        Toast.makeText(SignUpActivity.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                        hideDialog();
                        startMapsActivity();
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Registration Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Error in signing up. Check your details", Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user[first_name]", firstName);
                params.put("user[last_name]", lastName);
                params.put("user[email]", email);
                params.put("user[password]", password);
                params.put("user[password_confirmation]", passConfirm);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        // Adding request to request queue
        ProjectApp.getInstance().addToRequestQueue(registerRequest, tag_string_req);

    }



    private void startMapsActivity() {

        Intent intent = new Intent(SignUpActivity.this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // clears all previous activities task
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("first_name", firstName);
        intent.putExtra("last_name", lastName);
        intent.putExtra("email", inputEmail);
        intent.putExtra("image", mImageUri);
        startActivity(intent);
        finish();
    }


    private void showDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    private void hideDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                mSelectImage.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
