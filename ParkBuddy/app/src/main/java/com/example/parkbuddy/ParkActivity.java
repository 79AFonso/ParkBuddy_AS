package com.example.parkbuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.behavior.SwipeDismissBehavior;

import java.util.ArrayList;
import java.util.List;

public class ParkActivity extends AppCompatActivity {

    private List<String> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);

        // Set up the list view and the adapter
        ListView listView = findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        // Set up the add button
        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the text from the edit text and add it to the list
                EditText editText = findViewById(R.id.edit_text);
                String item = editText.getText().toString();
                items.add(item);
                adapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                items.remove(i);
                adapter.notifyDataSetChanged();
            }
        });

    }
}
