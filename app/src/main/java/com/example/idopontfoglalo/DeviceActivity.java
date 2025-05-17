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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.idopontfoglalo.adapters.DeviceAdapter;

import java.util.ArrayList;
import java.util.List;

public class DeviceActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDevices;
    private DeviceAdapter deviceAdapter;
    private List<Device> deviceList = new ArrayList<>();
    private EditText editTextBrand, editTextModel;
    private Spinner spinnerType;

    private String editingDeviceId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        recyclerViewDevices = findViewById(R.id.recyclerViewDevices);
        recyclerViewDevices.setLayoutManager(new LinearLayoutManager(this));
        deviceAdapter = new DeviceAdapter(deviceList, new DeviceAdapter.DeviceActionListener() {@Override
        public void onEdit(Device device, int position) {
            editTextBrand.setText(device.getBrand());
            editTextModel.setText(device.getModel());
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerType.getAdapter();
            int spinnerPosition = adapter.getPosition(device.getType());
            spinnerType.setSelection(spinnerPosition);

            editingDeviceId = device.getId();
            android.widget.Button saveButton = findViewById(R.id.buttonSaveDevice);
            saveButton.setText("Frissítés");
            saveButton.setOnClickListener(v -> updateDevice());
        }

            @Override
            public void onDelete(Device device, int position) {
                FirebaseFirestore.getInstance().collection("devices").document(device.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(DeviceActivity.this, "Törölve!", Toast.LENGTH_SHORT).show();
                            loadDevices();
                        });
            }
        });
        recyclerViewDevices.setAdapter(deviceAdapter);

        loadDevices();

        editTextBrand = findViewById(R.id.editTextBrand);
        editTextModel = findViewById(R.id.editTextModel);
        spinnerType = findViewById(R.id.spinnerType);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.Device);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.Profile) {
                startActivity(new Intent(this, MainPageActivity.class));
                return true;
            } else if (id == R.id.Device) {
                return true;
            } else if (id == R.id.ServiceType) {
                startActivity(new Intent(this, ServiceTypeActivity.class));
                return true;
            } else if (id == R.id.appointmentBooking) {
                startActivity(new Intent(this, AppointmentActivity.class));
                return true;
            } else if (id == R.id.logout) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            }
            return false;
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.device_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        findViewById(R.id.buttonSaveDevice).setOnClickListener(this::saveDevice);
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

        android.widget.Button saveButton = findViewById(R.id.buttonSaveDevice);
        saveButton.setText("Mentés");
        editTextBrand.setText("");
        editTextModel.setText("");
        spinnerType.setSelection(0);
        editingDeviceId = null;
    }

    private void updateDevice() {
        String brand = editTextBrand.getText().toString().trim();
        String model = editTextModel.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();

        if (brand.isEmpty() || model.isEmpty()) {
            Toast.makeText(this, "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
            return;
        }

        Device updatedDevice = new Device(brand, editingDeviceId, model, type);

        FirebaseFirestore.getInstance().collection("devices").document(editingDeviceId)
                .set(updatedDevice)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Frissítve!", Toast.LENGTH_SHORT).show();
                    loadDevices();
                    android.widget.Button saveButton = findViewById(R.id.buttonSaveDevice);
                    saveButton.setText("Mentés");
                    editTextBrand.setText("");
                    editTextModel.setText("");
                    spinnerType.setSelection(0);
                    editingDeviceId = null;
                    saveButton.setOnClickListener(this::saveDevice);
                });
    }

    private void loadDevices() {
        FirebaseFirestore.getInstance().collection("devices")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    deviceList.clear();
                    for (var doc : queryDocumentSnapshots) {
                        Device device = doc.toObject(Device.class);
                        deviceList.add(device);
                    }
                    deviceAdapter.notifyDataSetChanged();
                });
    }
}