package com.carloslopezmari.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

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

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    Button accessButton;
    TextInputEditText usernameEditText;
    TextInputEditText passwordEditText;
    TextView signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        accessButton = findViewById(R.id.access);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        signup = findViewById(R.id.signUp);

        accessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = Constants.BASE_PATH + Constants.USER_LOGIN;

                final NetworkResponse networkResponse;

                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("username", usernameEditText.getText());
                    jsonBody.put("password", passwordEditText.getText());
                    final String requestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.startsWith("Bearer")){
                                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                                editor.putString("token", response.replace("Bearer ", ""));
                                editor.commit();
                                ActivityChooser.chooseAndStartActivity(LoginActivity.this);
                            } else {
                                Toast.makeText(getApplicationContext(), "Respuesta del servidor desconocida, por favor, contacta con el administrador.", Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse.statusCode == 403){
                                Toast.makeText(getApplicationContext(), "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show();
                                return;
                            }

                            Toast.makeText(getApplicationContext(), "Se ha producido un error: "+ error.toString(), Toast.LENGTH_LONG).show();

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

                            return Response.success(response.headers.get("Authorization"), HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };

                    requestQueue.add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });


    }


}