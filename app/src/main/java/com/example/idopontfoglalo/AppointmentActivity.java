package com.example.idopontfoglalo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.idopontfoglalo.models.Appointment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {
    private EditText editTextDate;
    private Spinner spinnerDevice, spinnerService, spinnerStatus;

    private List<String> deviceIds = new ArrayList<>();
    private List<String> serviceIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        editTextDate = findViewById(R.id.editTextDate);
        spinnerDevice = findViewById(R.id.spinnerDevice);
        spinnerService = findViewById(R.id.spinnerService);
        spinnerStatus = findViewById(R.id.spinnerStatus);

        editTextDate.setOnClickListener(v -> showDateTimePicker());

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                this, R.array.appointment_status, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        // Eszközök betöltése Firestore-ból
        FirebaseFirestore.getInstance().collection("devices")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> deviceNames = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        deviceNames.add(doc.getString("brand") + " " + doc.getString("model"));
                        deviceIds.add(doc.getString("id"));
                    }
                    ArrayAdapter<String> deviceAdapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, deviceNames);
                    deviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDevice.setAdapter(deviceAdapter);
                });

        // Szerviztípusok betöltése Firestore-ból
        FirebaseFirestore.getInstance().collection("serviceTypes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> serviceNames = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        serviceNames.add(doc.getString("name"));
                        serviceIds.add(doc.getId());
                    }
                    ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, serviceNames);
                    serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerService.setAdapter(serviceAdapter);
                });
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

    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            (timeView, hourOfDay, minute) -> {
                                String dateTime = String.format("%04d.%02d.%02d %02d:%02d", year, month + 1, dayOfMonth, hourOfDay, minute);
                                editTextDate.setText(dateTime);
                            },
                            calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void saveAppointment(View view) {
        String dateStr = editTextDate.getText().toString();
        String status = spinnerStatus.getSelectedItem().toString();

        int devicePos = spinnerDevice.getSelectedItemPosition();
        int servicePos = spinnerService.getSelectedItemPosition();

        if (dateStr.isEmpty() || devicePos < 0 || servicePos < 0) {
            Toast.makeText(this, "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
            return;
        }

        Timestamp date = Timestamp.now();
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy.MM.dd HH:mm");
            java.util.Date parsedDate = sdf.parse(dateStr);
            date = new Timestamp(parsedDate);
        } catch (Exception e) {
            Toast.makeText(this, "Hibás dátum formátum!", Toast.LENGTH_SHORT).show();
            return;
        }

        String deviceId = deviceIds.get(devicePos);
        String serviceId = serviceIds.get(servicePos);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Appointment appointment = new Appointment(date, deviceId, serviceId, status, userId);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("appointments")
                .add(appointment)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Sikeres mentés!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}