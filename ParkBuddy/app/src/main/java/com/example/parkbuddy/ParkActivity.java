package com.example.parkbuddy;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ParkActivity extends AppCompatActivity {

    private List<VehicleModel> arrVehicles = new ArrayList<>();
    private VehicleModelAdapter adapter;
    FloatingActionButton btnOpenDialog;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);

        btnOpenDialog = findViewById(R.id.btnDialog);

        recyclerView = findViewById(R.id.recycler_view);



        // o nosso vermelho
        int color = Color.parseColor("#A0282C");

        // Get the app bar for the activity
        ActionBar appBar = getSupportActionBar();
        appBar.setBackgroundDrawable(new ColorDrawable(color));
        appBar.setTitle("Park");

        // Enable the "back" button in the app bar
        appBar.setDisplayHomeAsUpEnabled(true);

        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ParkActivity.this);  // sitio onde abre o dialog
                dialog.setContentView(R.layout.add_dialog);

                EditText txtModel = dialog.findViewById(R.id.txtModel);
                EditText txtPlate = dialog.findViewById(R.id.txtPlate);
                Button btnAdd = dialog.findViewById(R.id.btnAdd);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       // se quiser adicionar verificacao aqui  -> if else
                       String model = txtModel.getText().toString();
                       String plate = txtPlate.getText().toString();

                       arrVehicles.add(new VehicleModel(model,plate));

                       adapter.notifyItemInserted(arrVehicles.size() - 1);

                       recyclerView.scrollToPosition(arrVehicles.size() - 1);

                       dialog.dismiss();
                   }
                });

                dialog.show();

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new VehicleModelAdapter(arrVehicles, this);
        recyclerView.setAdapter(adapter);
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
