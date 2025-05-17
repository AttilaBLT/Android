package com.example.idopontfoglalo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.idopontfoglalo.models.Device;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeviceActivity extends AppCompatActivity {

    private EditText editTextBrand, editTextModel;
    private Spinner spinnerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        editTextBrand = findViewById(R.id.editTextBrand);
        editTextModel = findViewById(R.id.editTextModel);
        spinnerType = findViewById(R.id.spinnerType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.device_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        findViewById(R.id.buttonSaveDevice).setOnClickListener(this::saveDevice);
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

    public void saveDevice(View view) {
        String brand = editTextBrand.getText().toString().trim();
        String model = editTextModel.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();

        if (brand.isEmpty() || model.isEmpty()) {
            Toast.makeText(this, "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = FirebaseFirestore.getInstance().collection("devices").document().getId();

        Device device = new Device(brand, id, model, type);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("devices")
                .document(id)
                .set(device)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Sikeres mentés!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}