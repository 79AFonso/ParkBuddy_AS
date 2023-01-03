package com.example.parkbuddy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button parkBtn = findViewById(R.id.parkBtn);
        Button scanQrBtn = findViewById(R.id.scanBtn);
        Button statisticBtn = findViewById(R.id.statisticsBtn);
        Button profileBtn = findViewById(R.id.profileBtn);

        // o nosso vermelho
        int color = Color.parseColor("#A0282C");

        // Get the app bar for the activity
        ActionBar appBar = getSupportActionBar();
        appBar.setBackgroundDrawable(new ColorDrawable(color));
        appBar.setTitle("Menu");
        // cor dos butoes
        parkBtn.setBackgroundColor(Color.WHITE);
        parkBtn.setTextColor(Color.BLACK);
        scanQrBtn.setBackgroundColor(Color.WHITE);
        scanQrBtn.setTextColor(Color.BLACK);
        statisticBtn.setBackgroundColor(Color.WHITE);
        statisticBtn.setTextColor(Color.BLACK);
        profileBtn.setBackgroundColor(Color.WHITE);
        profileBtn.setTextColor(Color.BLACK);


        // Check if the location permission has been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            // Request the location permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        }

        parkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do something when the button is clicked
                Intent intent = new Intent(MainMenu.this, ParkActivity.class);
                startActivity(intent);
            }
        });

        statisticBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do something when the button is clicked
                Intent intent = new Intent(MainMenu.this, SimplePedometerActivity.class);
                startActivity(intent);
            }
        });

        scanQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do something when the button is clicked
                Intent intent = new Intent(MainMenu.this, ScannerActivity.class);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do something when the button is clicked
                Intent intent = new Intent(MainMenu.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}