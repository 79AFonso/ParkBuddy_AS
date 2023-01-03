package com.example.parkbuddy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    Button btnLogOut, btnEditPassword, btnEditMail;
    FirebaseAuth mAuth;
    TextView textMail, textparkedCars;
    int count = 0;
    SensorManager sensorManager;
    Sensor proximitySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // o nosso vermelho
        int color = Color.parseColor("#A0282C");

        // Get the app bar for the activity
        ActionBar appBar = getSupportActionBar();
        appBar.setBackgroundDrawable(new ColorDrawable(color));
        appBar.setTitle("Profile");

        // Enable the "back" button in the app bar
        appBar.setDisplayHomeAsUpEnabled(true);

        btnLogOut = findViewById(R.id.btnLogout);
        btnEditPassword = findViewById(R.id.btnEditPassword);
        btnEditMail = findViewById(R.id.btnEditEmail);
        textMail = findViewById(R.id.textEmail);
        textparkedCars = findViewById(R.id.textParkedCars);
        mAuth = FirebaseAuth.getInstance();

        textMail.setText(mAuth.getCurrentUser().getEmail());



        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        });

        btnEditPassword.setOnClickListener(view ->{
            openDialog();
        });
        btnEditMail.setOnClickListener(view ->{
            openDialogMail();
        });



    }


    // calling the sensor event class to detect
    // the change in data when sensor starts working.
    SensorEventListener proximitySensorEventListener = new SensorEventListener() {


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // method to check accuracy changed in sensor.
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            // check if the sensor type is proximity sensor.
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0) {
                    // here we are setting our status to our textview..
                    // if sensor event return 0 then object is closed
                    // to sensor else object is away from sensor.
                    System.out.println("Close");
                    layoutParams.screenBrightness = (float) 0.1;
                    getWindow().setAttributes(layoutParams);
                } else {
                    layoutParams.screenBrightness = (float) 0.6;
                    getWindow().setAttributes(layoutParams);
                    System.out.println("Away");
                }
            }
        }
    };

    private void openDialog() {
        PwDialog pwDialog = new PwDialog();
        pwDialog.show(getSupportFragmentManager(), "Change Password");
    }
    private void openDialogMail() {
        EmailDialog emailDialog = new EmailDialog();
        emailDialog.show(getSupportFragmentManager(), "Change Email");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle "up" button click
        if (item.getItemId() == android.R.id.home) {
            // Navigate the user back to the previous activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        }

        // Create a reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://parkbuddy-1b971-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference itemsRef = database.getReference();

        // Retrieve the data from the Realtime Database
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                // Get the current user
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String currentUserId = currentUser.getUid();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    VehicleModel item = snapshot.getValue(VehicleModel.class);
                    // Only include data for the current user

                    if (item.getUserId().equals(currentUserId)) {
                        count++;
                    }
                }
                textparkedCars.setText("You have "+count+" parked cars at the moment");

            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Handle the error
            }

        });

        // calling sensor service.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        }

        // from sensor service we are
        // calling proximity sensor
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // handling the case if the proximity
        // sensor is not present in users device.
        if (proximitySensor == null) {
            Toast.makeText(this, "No proximity sensor found in device.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // registering our sensor with sensor manager.
            sensorManager.registerListener(proximitySensorEventListener,
                    proximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }



    }
    
    
}
