package com.example.parkbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class VehicleModelAdapter extends RecyclerView.Adapter<VehicleModelAdapter.VehicleModelViewHolder> {
    // Step 1: Declare a private list of VehicleModel objects and a private context
    private List<VehicleModel> mVehicleModels;
    private Context mContext;

    // Step 2: Create a constructor that takes in a list of VehicleModel objects and a context
    public VehicleModelAdapter(List<VehicleModel> vehicleModels, Context context) {
        mVehicleModels = vehicleModels;
        mContext = context;
    }

    // Step 3: Override the onCreateViewHolder method
    @Override
    public VehicleModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(mContext).inflate(R.layout.vehicle_row, parent, false);
        return new VehicleModelViewHolder(view);
    }

    // Step 4: Override the onBindViewHolder method
    @Override
    public void onBindViewHolder(VehicleModelViewHolder holder, int position) {
        // Get the VehicleModel object at the given position
        VehicleModel vehicleModel = (VehicleModel) mVehicleModels.get(position);

        // Set the text and image for the views
        holder.modelTextView.setText(vehicleModel.model);
        holder.plateTextView.setText(vehicleModel.plate);
        Glide.with(mContext).load(vehicleModel.img).into(holder.imageView);
    }

    // Step 5: Override the getItemCount method
    @Override
    public int getItemCount() {
        return mVehicleModels.size();
    }

    // Step 6: Create a static inner class called VehicleModelViewHolder that extends RecyclerView.ViewHolder
    static class VehicleModelViewHolder extends RecyclerView.ViewHolder {
        // Step 6a: Declare ImageView, TextView, and TextView variables
        ImageView imageView;
        TextView modelTextView;
        TextView plateTextView;

        // Step 6b: Create a constructor that takes in a View as a parameter
        public VehicleModelViewHolder(View itemView) {
            super(itemView);
            // Step 6c: Initialize the ImageView, TextView, and TextView variables
            imageView = itemView.findViewById(R.id.imageView2);
            modelTextView = itemView.findViewById(R.id.txtModel);
            plateTextView = itemView.findViewById(R.id.txtPlate);
        }
    }
}
