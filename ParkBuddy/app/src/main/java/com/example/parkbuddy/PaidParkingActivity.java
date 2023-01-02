package com.example.parkbuddy;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PaidParkingActivity extends AppCompatActivity {

    private static final String TAG = "PaidParkingActivity";

    private TextView txtTimer;
    private Button btnStart;
    private Button btnStop;

    // Flag to indicate if the service is bound
    private boolean isBound = false;

    // Service connection to bind to the service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TimerService.TimerBinder binder = (TimerService.TimerBinder) service;
            timerService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    // Timer service
    private TimerService timerService;

    // Broadcast receiver to receive updates from the timer service
    private BroadcastReceiver timerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Update the timer value in the text view
            int timerValue = intent.getIntExtra("timerValue", 0);
            txtTimer.setText(String.valueOf(timerValue));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid);

        txtTimer = findViewById(R.id.txtView_timer);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);

        int color = Color.parseColor("#A0282C");
        ActionBar appBar = getSupportActionBar();
        appBar.setBackgroundDrawable(new ColorDrawable(color));
        appBar.setTitle("PAID PARKING");

        // Enable the "back" button in the app bar
        appBar.setDisplayHomeAsUpEnabled(true);

        // Bind to the TimerService
        Intent intent = new Intent(this, TimerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        // Set up click listeners for the buttons
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound) {
                    Intent serviceIntent = new Intent(PaidParkingActivity.this, TimerService.class);
                    startService(serviceIntent);
                    timerService.startTimer();
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound) {
                    timerService.stopTimer();
                    Intent serviceIntent = new Intent(PaidParkingActivity.this, TimerService.class);
                    stopService(serviceIntent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the broadcast receiver to receive updates from the timer service
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.parkbuddy.TIMER_UPDATE");
        registerReceiver(timerReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the broadcast receiver
        unregisterReceiver(timerReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
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
