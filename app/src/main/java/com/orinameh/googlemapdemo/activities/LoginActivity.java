package com.orinameh.googlemapdemo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orinameh.googlemapdemo.R;
import com.orinameh.googlemapdemo.application.AppConfig;
import com.orinameh.googlemapdemo.application.ProjectApp;
import com.orinameh.googlemapdemo.helpers.SQLiteHandler;
import com.orinameh.googlemapdemo.helpers.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText emailLogin;
    private EditText passLogin;
    private Button btnLogin;
    private TextView createNew;
    private ProgressDialog dialog;
    private SessionManager sessionManager;
    private SQLiteHandler db;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = (EditText) findViewById(R.id.etEmail);
        passLogin = (EditText) findViewById(R.id.etPass);
        btnLogin = (Button) findViewById(R.id.sign_btn);
        createNew = (TextView) findViewById(R.id.create_new_acct);


        // Progress dialog
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        // Session manager
        sessionManager = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (sessionManager.isUserLoggedIn()) {
            // User is already logged in. Take him to map activity
            Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailLogin.getText().toString();
                password = passLogin.getText().toString();

                // Check for empty data in the form

                if (!email.isEmpty() && !password.isEmpty()) {

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        emailLogin.setError("Enter Valid Email Address");
                    }

                    // login user
                    checkLogin(email, password);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }
            }
        });

        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // clears all previous activities task
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish();
            }
        });
    }

    private void checkLogin(final String email, final String password) {

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        dialog.setMessage("Logging in....");
        showDialog();

        StringRequest loginReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Login Response: " + response);
                        Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                        hideDialog();

//                        try {
//                            JSONObject jObj = new JSONObject(response);
//                            boolean error = jObj.getBoolean("error");
//
//                            // Check for error node in json
//                            if (!error) {
//                                // user successfully logged in
//                                // Create login session
//                                sessionManager.setLogin(true);
//
//                                JSONObject user = jObj.getJSONObject("user");
//                                String firstName = user.getString("frist_name");
//                                String lastName = user.getString("last_name");
//                                String userEmail = user.getString("email");
//                            } else {
//                                String errorMsg = jObj.getString("error_msg");
//                                Toast.makeText(getApplicationContext(),
//                                        errorMsg, Toast.LENGTH_LONG).show();
//                            }
//                        } catch (JSONException e) {
//
//                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//
//                        }

                        startMapsActivity();

                    }

                }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Error in logging in, check your details", Toast.LENGTH_LONG).show();
                    hideDialog();
                }

            })  {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("user[email]", email);
                    params.put("user[password]", password);

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
        ProjectApp.getInstance().addToRequestQueue(loginReq, tag_string_req);

    }

//    This makes the application exits when the back button is pressed
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void startMapsActivity() {

        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // clears all previous activities task
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("email", email);
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
}
