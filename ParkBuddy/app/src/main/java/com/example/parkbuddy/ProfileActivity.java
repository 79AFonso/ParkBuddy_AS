package com.example.parkbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

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

    Button btnLogOut;
    FirebaseAuth mAuth;
    TextView textMail, textparkedCars;
    int count = 0;

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
        textMail = findViewById(R.id.textEmail);
        textparkedCars = findViewById(R.id.textParkedCars);
        mAuth = FirebaseAuth.getInstance();

        textMail.setText(mAuth.getCurrentUser().getEmail());



        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
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
                    System.out.println(item.getUserId());
                    System.out.println(currentUserId);
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



    }
}
