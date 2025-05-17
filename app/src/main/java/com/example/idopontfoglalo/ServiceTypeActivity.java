package com.example.idopontfoglalo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.idopontfoglalo.models.ServiceType;
import com.google.firebase.firestore.FirebaseFirestore;

public class ServiceTypeActivity extends AppCompatActivity {

    private EditText editTextServiceName, editTextServiceDuration, editTextServicePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicetype);

        editTextServiceName = findViewById(R.id.editTextServiceName);
        editTextServiceDuration = findViewById(R.id.editTextServiceDuration);
        editTextServicePrice = findViewById(R.id.editTextServicePrice);

        findViewById(R.id.buttonSaveServiceType).setOnClickListener(this::saveServiceType);
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

    public void saveServiceType(View view) {
        String name = editTextServiceName.getText().toString().trim();
        String durationStr = editTextServiceDuration.getText().toString().trim();
        String priceStr = editTextServicePrice.getText().toString().trim();

        if (name.isEmpty() || durationStr.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
            return;
        }

        int duration = Integer.parseInt(durationStr);
        int price = Integer.parseInt(priceStr);

        // Egyedi azonosító generálása (pl. Firestore auto ID)
        String id = FirebaseFirestore.getInstance().collection("serviceTypes").document().getId();

        ServiceType serviceType = new ServiceType(duration, name, price);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("serviceTypes")
                .document(id)
                .set(serviceType)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Sikeres mentés!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}