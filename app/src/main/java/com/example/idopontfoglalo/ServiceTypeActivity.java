package com.example.idopontfoglalo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idopontfoglalo.adapters.ServiceTypeAdapter;
import com.example.idopontfoglalo.models.ServiceType;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ServiceTypeActivity extends AppCompatActivity {

    private EditText editTextServiceName, editTextServiceDuration, editTextServicePrice;
    private RecyclerView recyclerViewServiceTypes;
    private ServiceTypeAdapter serviceTypeAdapter;
    private List<ServiceType> serviceTypeList = new ArrayList<>();
    private List<String> serviceTypeIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicetype);

        editTextServiceName = findViewById(R.id.editTextServiceName);
        editTextServiceDuration = findViewById(R.id.editTextServiceDuration);
        editTextServicePrice = findViewById(R.id.editTextServicePrice);
        recyclerViewServiceTypes = findViewById(R.id.recyclerViewServiceTypes);
        recyclerViewServiceTypes.setLayoutManager(new LinearLayoutManager(this));
        serviceTypeAdapter = new ServiceTypeAdapter(serviceTypeList, serviceTypeIds, this, this::editServiceType);
        recyclerViewServiceTypes.setAdapter(serviceTypeAdapter);

        findViewById(R.id.buttonSaveServiceType).setOnClickListener(this::saveServiceType);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.ServiceType);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.Profile) {
                startActivity(new Intent(this, MainPageActivity.class));
                return true;
            } else if (id == R.id.Device) {
                startActivity(new Intent(this, DeviceActivity.class));
                return true;
            } else if (id == R.id.ServiceType) {
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

        loadServiceTypes();
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

    private void loadServiceTypes() {
        FirebaseFirestore.getInstance().collection("serviceTypes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    serviceTypeList.clear();
                    serviceTypeIds.clear();
                    for (var doc : queryDocumentSnapshots) {
                        ServiceType st = doc.toObject(ServiceType.class);
                        serviceTypeList.add(st);
                        serviceTypeIds.add(doc.getId());
                    }
                    serviceTypeAdapter.notifyDataSetChanged();
                });
    }

    private void editServiceType(ServiceType st, String id) {
        editTextServiceName.setText(st.getName());
        editTextServiceDuration.setText(String.valueOf(st.getDuration()));
        editTextServicePrice.setText(String.valueOf(st.getPrice()));

        android.widget.Button saveButton = findViewById(R.id.buttonSaveServiceType);
        saveButton.setText("Módosítás");

        findViewById(R.id.buttonSaveServiceType).setOnClickListener(v -> {
            String name = editTextServiceName.getText().toString().trim();
            int duration = Integer.parseInt(editTextServiceDuration.getText().toString().trim());
            int price = Integer.parseInt(editTextServicePrice.getText().toString().trim());
            ServiceType updated = new ServiceType(duration, name, price);
            FirebaseFirestore.getInstance().collection("serviceTypes").document(id).set(updated)
                    .addOnSuccessListener(aVoid -> {
                        loadServiceTypes();
                        Toast.makeText(this, "Frissítve!", Toast.LENGTH_SHORT).show();
                        saveButton.setText("Mentés");
                        editTextServiceName.setText("");
                        editTextServiceDuration.setText("");
                        editTextServicePrice.setText("");
                        saveButton.setOnClickListener(this::saveServiceType);
                    });
        });
    }
}