package com.carloslopezmari.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.carloslopezmari.myapplication.Utils.ActivityChooser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

public class SignUpActivity extends AppCompatActivity {


    TextInputLayout usernameLayout;
    TextInputEditText username;
    TextInputLayout passwordLayout;
    TextInputEditText password;
    TextInputLayout passwordLayout2;
    TextInputEditText password2;
    Button signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        signup = findViewById(R.id.signupButton);

        username = findViewById(R.id.usernameSignUp);
        usernameLayout = findViewById(R.id.usernameSignUpLayout);
        password = findViewById(R.id.passwordSignUp);
        password2 = findViewById(R.id.passwordSignUp2);
        passwordLayout = findViewById(R.id.passwordSignUpLayout);
        passwordLayout2 = findViewById(R.id.passwordSignUpLayout2);


        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    usernameLayout.setError(null);
                } else if(username.getText().toString().length() < 6 && username.getText().toString().length() > 0){
                    usernameLayout.setError("Nombre de usuario demasiado corto");
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    passwordLayout.setError(null);
                } else if(password.getText().toString().length() < 6 && password.getText().toString().length() > 0){
                    passwordLayout.setError("Contraseña demasiado corta");
                } else if(!password2.getText().toString().equals(password.getText().toString()) && password2.getText().toString().length() > 0){
                    passwordLayout2.setError("Las contraseñas deben coincidir" );
                }
            }
        });

        password2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    passwordLayout2.setError(null);
                } else if(!password2.getText().toString().equals(password.getText().toString()) && password2.getText().toString().length() > 0){
                    passwordLayout2.setError("Las contraseñas deben coincidir");
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(password2.getText().toString().equals(password.getText().toString())){
                    passwordLayout2.setError(null);
                }

            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().length() == 0) {
                    usernameLayout.setError("Por favor, elige un nombre de usuario");
                }
                if(password.getText().toString().length() == 0){
                    passwordLayout.setError("Por favor, elige una contraseña");
                }
                if(password2.getText().toString().length() == 0){
                    passwordLayout2.setError("Por favor, repite la contraseña");
                }

                username.clearFocus();
                password.clearFocus();
                password2.clearFocus();

                if(usernameLayout.getError() == null && passwordLayout.getError() == null && passwordLayout2.getError() == null){

                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("username", username.getText());
                        jsonBody.put("password", password.getText());
                        final String requestBody = jsonBody.toString();
                        String url = Constants.BASE_PATH + Constants.USER_SIGN_UP;
                        final int[] statusCode = new int[1];

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (statusCode[0] == HttpURLConnection.HTTP_CREATED){
                                    Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    Toast.makeText(getApplicationContext(), "Cuenta creada satisfactoriamente", Toast.LENGTH_SHORT).show();
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse.statusCode == 409){
                                    usernameLayout.setError(new String(error.networkResponse.data));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Se ha producido un error: "+ error.toString(), Toast.LENGTH_LONG).show();

                                }


                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }


                            @Override
                            public byte[] getBody() {
                                try {
                                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                    return null;
                                }
                            }

                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                statusCode[0] = response.statusCode;
                                return super.parseNetworkResponse(response);
                            }
                        };

                        requestQueue.add(stringRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });






    }

}