package com.example.parkbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // o nosso vermelho
        int color = Color.parseColor("#A0282C");

        // Get the app bar for the activity
        ActionBar appBar = getSupportActionBar();
        appBar.setBackgroundDrawable(new ColorDrawable(color));

        Button signInBtn = findViewById(R.id.signInBtn);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(MainActivity.this, "Authentication error : "+errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                        .setSubtitle("Login using fingerprint Authentication")
                                .setNegativeButtonText("User App password")
                                        .build();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                // Do something when the button is clicked

                FingerprintManager fingerprintManager = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
                }
                if (fingerprintManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!fingerprintManager.isHardwareDetected()) {
                            // Device doesn't support fingerprint authentication
                        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                            // User hasn't enrolled any fingerprints to authenticate with
                        } else {
                            // Everything is ready for fingerprint authentication
                            biometricPrompt.authenticate(promptInfo);
                        }
                    }
                }

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}