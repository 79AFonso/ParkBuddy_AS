package com.example.parkbuddy;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class PaidParkingActivity extends AppCompatActivity {



    private static final String TAG = "PaidParkingActivity";

    private TextView txtTimer;
    private Button btnStart;
    private Button btnStop;

    TextInputEditText pricePerHour;

    int timerValue;
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
            timerValue = intent.getIntExtra("timerValue", 0);
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

        pricePerHour = findViewById(R.id.txtPreco);

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
                // Create an Intent for the TimerService
                Intent serviceIntent = new Intent(PaidParkingActivity.this, TimerService.class);
                serviceIntent.putExtra("pricePerHour", pricePerHour.getText().toString());

                // Start the foreground service
                ContextCompat.startForegroundService(PaidParkingActivity.this, serviceIntent);

                // Bind to the service
                bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pricePerHour.getText().toString().equals("")) {
                    Toast.makeText(PaidParkingActivity.this, "Please insert Price per hour",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    float preco = Float.parseFloat(pricePerHour.getText().toString());

                    float time = timerValue;  // segundos

                    float hora = time /3600; // segundos para hora

                    float precoF = preco * hora;  // preço final

                    if (isBound) {
                        timerService.stopTimer();
                        Intent serviceIntent = new Intent(PaidParkingActivity.this, TimerService.class);
                        stopService(serviceIntent);
                    }

                    // Create the object of AlertDialog Builder class
                    AlertDialog.Builder builder = new AlertDialog.Builder(PaidParkingActivity.this);

                    // Set the message show for the Alert time
                    builder.setMessage("Do you want to pay ?");

                    // Set Alert Title
                    builder.setTitle("PAID PARKING");

                    // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                    builder.setCancelable(false);

                    // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
                    builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                        // When the user click yes button then app will close
                        Intent intent = new Intent(PaidParkingActivity.this, PaymentActivity.class);
                        intent.putExtra("preco", String.valueOf(String.format("%.2f", precoF)));
                        startActivity(intent);
                    });

                    // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
                    builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                        // If user click no then dialog box is canceled.
                        dialog.cancel();
                    });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();
                    // Show the Alert Dialog box
                    alertDialog.show();
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
