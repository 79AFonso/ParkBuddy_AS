package com.example.parkbuddy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class InfoActivity extends AppCompatActivity {

    ImageView vehicleImage;
    Button shareBtn;
    Button paidBtn;
    Button showMapBtn;
    TextView txtInfo;

    double latitude;
    double longitude;
    String fullAddress;

    VehicleModel actualVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Create a reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://parkbuddy-1b971-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference itemsRef = database.getReference();


        vehicleImage = (ImageView) findViewById(R.id.info_image);
        shareBtn = (Button) findViewById(R.id.btn_share);
        showMapBtn = (Button) findViewById(R.id.btn_map);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        paidBtn = (Button) findViewById(R.id.btn_paid);

        // o nosso vermelho
        int color = Color.parseColor("#A0282C");

        Intent myIntent = getIntent(); // gets the previously created intent
        String matricula = myIntent.getStringExtra("matricula");
        Log.d("InfoActivity", "Matricula: " + matricula);

        // Get the app bar for the activity
        ActionBar appBar = getSupportActionBar();
        appBar.setBackgroundDrawable(new ColorDrawable(color));
        appBar.setTitle("Info");

        // Enable the "back" button in the app bar
        appBar.setDisplayHomeAsUpEnabled(true);

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
                        if(item.getPlate().equals(matricula))
                        {
                            actualVehicle = item;
                            if(actualVehicle.getImageUrl().equals("No Image"))
                            {
                                if (!isFinishing()) {
                                    Glide.with(InfoActivity.this).load(R.drawable.logo_no_background).into(vehicleImage);
                                }
                            }else {
                                if (!isFinishing()) {
                                    Glide.with(InfoActivity.this).load(actualVehicle.getImageUrl()).into(vehicleImage);
                                }
                            }
                        }
                    }
                }

                latitude = actualVehicle.getLatitude();
                longitude = actualVehicle.getLongitude();

                Geocoder geocoder = new Geocoder(InfoActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    //String placeName = address.getLocality();
                    fullAddress = address.getAddressLine(0);
                    txtInfo.setText(fullAddress);
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle the error
            }

        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goShare = new Intent(InfoActivity.this, ShareQrActivity.class);
                goShare.putExtra("matricula", matricula);
                startActivity(goShare);
            }
        });

        paidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, PaidParkingActivity.class);
                intent.putExtra("matricula", matricula);
                startActivity(intent);
            }
        });

        showMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, MapsActivity.class);
                intent.putExtra("localizacao", latitude+" "+longitude+ " "+ matricula);
                startActivity(intent);
            }
        });

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
}
