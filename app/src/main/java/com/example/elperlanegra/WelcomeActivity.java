package com.example.elperlanegra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    ProgressBar pb_welcome;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        auth =  FirebaseAuth.getInstance();
        pb_welcome = findViewById(R.id.pb_wel);
        pb_welcome.setVisibility(View.GONE);

        if (auth.getCurrentUser() != null){
            pb_welcome.setVisibility(View.VISIBLE);
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            Toast.makeText(this, "¡SESIÓN INICIADA!", Toast.LENGTH_SHORT).show();
        }
    }

    public void registrarse(View view) {
        startActivity(new Intent(WelcomeActivity.this, RegistroActivity.class));
    }

    public void loginWelcome(View view) {
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
    }
}