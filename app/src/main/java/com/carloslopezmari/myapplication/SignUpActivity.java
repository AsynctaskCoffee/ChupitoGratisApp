package com.carloslopezmari.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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

                    Toast.makeText(SignUpActivity.this, "OK", Toast.LENGTH_SHORT).show();

                }
            }
        });






    }

}