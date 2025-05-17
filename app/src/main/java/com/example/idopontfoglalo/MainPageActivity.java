package com.example.idopontfoglalo;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.idopontfoglalo.adapters.AppointmentAdapter;
import com.example.idopontfoglalo.models.Appointment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.idopontfoglalo.models.AppointmentDisplay;

public class MainPageActivity extends AppCompatActivity {
    private RecyclerView recyclerViewAppointments;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointment> appointmentList = new ArrayList<>();
    private FirebaseUser user;
    private List<AppointmentDisplay> displayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.main);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.Profile) {
                return true;
            } else if (id == R.id.Device) {
                startActivity(new Intent(this, DeviceActivity.class));
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

        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments);
        recyclerViewAppointments.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        appointmentAdapter = new AppointmentAdapter(displayList);
        recyclerViewAppointments.setAdapter(appointmentAdapter);

        loadAppointments();
    }

    private void loadAppointments() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        FirebaseFirestore.getInstance().collection("appointments")
                .whereEqualTo("userId", user.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    displayList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Appointment appointment = doc.toObject(Appointment.class);

                        FirebaseFirestore.getInstance().collection("serviceTypes")
                                .document(appointment.getServiceId())
                                .get()
                                .addOnSuccessListener(serviceDoc -> {
                                    String serviceName = serviceDoc.exists() ? serviceDoc.getString("name") : "";

                                    FirebaseFirestore.getInstance().collection("devices")
                                            .document(appointment.getDeviceId())
                                            .get()
                                            .addOnSuccessListener(deviceDoc -> {
                                                String deviceName = "";
                                                if (deviceDoc.exists()) {
                                                    String brand = deviceDoc.getString("brand");
                                                    String model = deviceDoc.getString("model");
                                                    deviceName = (brand != null ? brand : "") + " " + (model != null ? model : "");
                                                }
                                                String dateString = "";
                                                if (appointment.getDate() != null) {
                                                    dateString = appointment.getDate().toDate().toString();
                                                }
                                                displayList.add(new AppointmentDisplay(dateString, appointment.getStatus(), serviceName, deviceName));
                                                appointmentAdapter.notifyDataSetChanged();
                                            });
                                });
                    }
                });
    }
}