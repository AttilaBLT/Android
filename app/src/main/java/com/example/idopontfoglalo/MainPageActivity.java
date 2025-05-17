package com.example.idopontfoglalo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainPageActivity extends AppCompatActivity {

    private static final String TAG = MainPageActivity.class.getName();
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    public void openAppointmentActivity(MenuItem item) {
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);
    }

    public void logout(MenuItem item) {
        finish();
    }

    public void openDeviceActivity(MenuItem item) {
        Intent intent = new Intent(this, DeviceActivity.class);
        startActivity(intent);
    }

    public void openServiceTypeActivity(MenuItem item) {
        Intent intent = new Intent(this, ServiceTypeActivity.class);
        startActivity(intent);
    }
}