package com.example.parkbuddy;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.parkbuddy.channelApp.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {

    // Binder given to clients
    private final IBinder binder = (IBinder) new TimerBinder();

    // Timer value
    private int timerValue = 0;

    // Timer task to update the timer value
    private TimerTask timerTask;

    // Timer to schedule the timer task
    private Timer timer;

    // Flag to indicate if the service is running
    private boolean isRunning = false;

    public class TimerBinder extends Binder {
        TimerService getService() {
            // Return this instance of TimerService so clients can call public methods
            return TimerService.this;
        }
    }

    // Binder object
    private final IBinder timerBinder = new TimerBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return timerBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (!isRunning) {

            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("PAID PARKING TIMER")
                    .setContentText(String.valueOf(timerValue))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1, notification);
            startTimer();
            isRunning = true;
        }

        // Return "sticky" to keep the service running until it is explicitly stopped
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        stopTimer();
        isRunning = false;
    }

    // Start the timer
    public void startTimer() {
        Log.d("Start","Start timer");
        // Start the timer
        if (!isRunning) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Log.d(TAG, "Timer task started");
                    // Update the timer value
                    timerValue++;
                    Intent timerIntent = new Intent();
                    timerIntent.setAction("com.example.parkbuddy.TIMER_UPDATE");
                    timerIntent.putExtra("timerValue", timerValue);
                    sendBroadcast(timerIntent);
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 1000); // Update the timer value every second
            isRunning = true;
        }
    }

    // Stop the timer
    public void stopTimer() {
        if (isRunning) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (timerTask != null) {
                timerTask.cancel();
                timerTask = null;
            }
            isRunning = false;
        }
    }

    // Get the current timer value
    public int getTimerValue() {
        return timerValue;
    }


}

