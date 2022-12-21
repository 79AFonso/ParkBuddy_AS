package com.example.parkbuddy;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

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
        // Get the adapter position of the ViewHolder
        int adapterPosition = holder.getAdapterPosition();

        // Get the VehicleModel object at the given position
        VehicleModel vehicleModel = (VehicleModel) mVehicleModels.get(adapterPosition);

        // Set the text and image for the views
        holder.modelTextView.setText(vehicleModel.model);
        holder.plateTextView.setText(vehicleModel.plate);
        Glide.with(mContext).load(vehicleModel.img).into(holder.imageView);

        holder.llRow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(mContext, InfoActivity.class);
               intent.putExtra("matricula", vehicleModel.plate);
               mContext.startActivity(intent);

           }
        });

        holder.llRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.add_dialog);

                EditText txtModel = dialog.findViewById(R.id.txtModel);
                EditText txtPlate = dialog.findViewById(R.id.txtPlate);
                Button btnAdd = dialog.findViewById(R.id.btnAdd);
                TextView txtTitle = dialog.findViewById(R.id.txtTitle);

                txtTitle.setText("Update Vehicle");

                btnAdd.setText("Update");

                txtModel.setText((mVehicleModels.get(adapterPosition)).model);
                txtPlate.setText((mVehicleModels.get(adapterPosition)).plate);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String model = txtModel.getText().toString();
                        String plate = txtPlate.getText().toString();

                        mVehicleModels.set(adapterPosition,new VehicleModel(model,plate));
                        notifyItemChanged(adapterPosition);

                        dialog.dismiss();

                    }
                });
                dialog.show();

                return true;
            }
        });
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
        LinearLayout llRow;

        // Step 6b: Create a constructor that takes in a View as a parameter
        public VehicleModelViewHolder(View itemView) {
            super(itemView);
            // Step 6c: Initialize the ImageView, TextView, and TextView variables
            imageView = itemView.findViewById(R.id.imageView2);
            modelTextView = itemView.findViewById(R.id.txtModel);
            plateTextView = itemView.findViewById(R.id.txtPlate);
            llRow = itemView.findViewById(R.id.llrow);
        }
    }
}


/*
para deletar
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                        .setTitle("Delete Vehicle")
                        .setMessage("Are you sure you want to delete?")
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mVehicleModels.remove(adapterPosition);
                                notifyItemRemoved(adapterPosition);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                builder.show();


                return true;
 */
