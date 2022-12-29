package com.example.parkbuddy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class ShareQrActivity extends AppCompatActivity {

    double latitude;
    double longitude;

    // o que vai ter no qr
    String data = "Waiting for data";

    VehicleModel actualVehicle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_qr);


        // o nosso vermelho
        int color = Color.parseColor("#A0282C");

        // Get the app bar for the activity
        ActionBar appBar = getSupportActionBar();
        appBar.setBackgroundDrawable(new ColorDrawable(color));
        appBar.setTitle("Share");

        // Enable the "back" button in the app bar
        appBar.setDisplayHomeAsUpEnabled(true);

        ImageView imageView = findViewById(R.id.image_shareqr);

        Intent myIntent = getIntent(); // gets the previously created intent
        String matricula = myIntent.getStringExtra("matricula");

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
                        if(item.getPlate().equals(matricula))
                        {
                            actualVehicle = item;
                            latitude = actualVehicle.getLatitude();
                            longitude = actualVehicle.getLongitude();

                            data = latitude + "," + longitude;

                            try {
                                Paint paint = new Paint();

                                // Encode the data and get a BitMatrix object
                                BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, 2000, 2000);

                                // Convert the BitMatrix object to a Bitmap object
                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                                Canvas canvas = new Canvas(bitmap);

                                // Set the color of the canvas to the desired color
                                //canvas.drawColor(Color.);
                                paint.setColor(color);

                                // Draw the QR code on top of the canvas
                                for (int y = 0; y < bitMatrix.getHeight(); y++) {
                                    for (int x = 0; x < bitMatrix.getWidth(); x++) {
                                        if (bitMatrix.get(x, y)) {
                                            canvas.drawPoint(x, y, paint);
                                        }
                                    }
                                }




                                // Set the Bitmap object as the content of the ImageView
                                imageView.setImageBitmap(bitmap);
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle the error
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
