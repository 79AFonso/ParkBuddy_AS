package com.example.parkbuddy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;



import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ParkActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1234;
    private static final int CAPTURE_CODE = 1001;
    private List<VehicleModel> arrVehicles = new ArrayList<>();
    private VehicleModelAdapter adapter;
    FloatingActionButton btnOpenDialog;
    RecyclerView recyclerView;
    DatabaseReference databaseUsers;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Uri image_uri;
    private Button btnTakePicture;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");


        btnOpenDialog = findViewById(R.id.btnDialog);
        recyclerView = findViewById(R.id.recycler_view);
        databaseUsers = FirebaseDatabase.getInstance().getReference();

        // o nosso vermelho
        int color = Color.parseColor("#A0282C");

        // Get the app bar for the activity
        ActionBar appBar = getSupportActionBar();
        appBar.setBackgroundDrawable(new ColorDrawable(color));
        appBar.setTitle("Park");

        // Enable the "back" button in the app bar
        appBar.setDisplayHomeAsUpEnabled(true);

        // Create a reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://parkbuddy-1b971-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference itemsRef = database.getReference();

        // Initialize the adapter in onCreate
        adapter = new VehicleModelAdapter(arrVehicles,this,recyclerView);
        recyclerView.setAdapter(adapter);

        // Retrieve the data from the Realtime Database
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the arrVehicles list
                arrVehicles.clear();

                // Get the current user
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String currentUserId = currentUser.getUid();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    VehicleModel item = snapshot.getValue(VehicleModel.class);
                    // Only include data for the current user
                    if (item.getUserId().equals(currentUserId)) {
                        arrVehicles.add(item);
                    }
                }

                // Notify the adapter of the data change
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Handle the error
            }

        });

        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ParkActivity.this);  // sitio onde abre o dialog
                dialog.setContentView(R.layout.add_dialog);

                EditText txtModel = dialog.findViewById(R.id.txtModel);
                EditText txtPlate = dialog.findViewById(R.id.txtPlate);
                Button btnAdd = dialog.findViewById(R.id.btnAdd);
                btnTakePicture = dialog.findViewById(R.id.btnImage);

                btnTakePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("picture", "btnTakePicture click");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                                        PackageManager.PERMISSION_DENIED ||
                                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                                PackageManager.PERMISSION_DENIED) {

                                String[] permissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permissions, PERMISSION_CODE);
                            }
                            else {
                                openCamera();
                            }
                        } else {
                            openCamera();
                        }
                    }
                });



                btnAdd.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       // se quiser adicionar verificacao aqui  -> if else
                       String model = txtModel.getText().toString();
                       String plate = txtPlate.getText().toString();
                       String img = String.valueOf(image_uri);

                       Boolean inUse = false;


                       for(int i = 0; i < arrVehicles.size(); i++) {
                           if(plate.equals(arrVehicles.get(i).getPlate())) {
                               inUse = true;
                               Toast.makeText(ParkActivity.this, "Car Plate already in use",Toast.LENGTH_SHORT).show();

                           }
                       }
                       if(!inUse)
                       {
                           arrVehicles.add(new VehicleModel(img,model,plate,currentUser.getUid()));
                           // Upload(plate, taskSnapshot.getStorage().getDownloadUrl().toString(), currentUser,model);

                           uploadFile(img,model,plate);

                           adapter.notifyItemInserted(arrVehicles.size() - 1);

                           recyclerView.scrollToPosition(arrVehicles.size() - 1);

                           dialog.dismiss();
                       }
                   }
                });

                dialog.show();

            }
        });



        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        Log.d("ARRAY VEHICLES", arrVehicles.toString());

        adapter = new VehicleModelAdapter(arrVehicles, this,recyclerView);
        recyclerView.setAdapter(adapter);



    }

    private void openCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "new image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent camintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camintent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(camintent, CAPTURE_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else {
                    Toast.makeText(ParkActivity.this, "No Permission", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            //imageView.setImageURI(image_uri);
            Toast.makeText(this, image_uri.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // Delete the item from your data source
            int position = viewHolder.getAdapterPosition();
            VehicleModel model = arrVehicles.get(position);
            String plate = String.valueOf(model.getPlate());

            // Delete the item from the database
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://parkbuddy-1b971-default-rtdb.europe-west1.firebasedatabase.app");
            DatabaseReference db = database.getReference(plate);


            String fileUrl = model.getImageUrl();
            Log.d("fileURLLLLL", fileUrl);

            if(!fileUrl.equals("No Image")) {
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl);
                // Delete the file
                storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // An error occurred while deleting the file
                        Toast.makeText(ParkActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            db.removeValue();
            arrVehicles.remove(position);

            Toast.makeText(ParkActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();


            // Notify the adapter of the change
            adapter.notifyItemRemoved(position);
        }
    };

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

    private void InsertData(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("userID", "InsertData: " + currentUser.getUid());
        // https://www.youtube.com/watch?v=MfCiiTEwt3g
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private String imageUrl;

    private void uploadFile(String img, String model, String plate){
        String currentUser = mAuth.getCurrentUser().getUid();  // tentar referenciar estes 2 fora
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://parkbuddy-1b971-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseUsers = database.getReference();
        if (image_uri != null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(image_uri));


            fileReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for the image
                                    imageUrl = uri.toString();
                                    Log.d("imageUrlWHAT",imageUrl);

                                    Toast.makeText(ParkActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                    Upload upload = new Upload(plate, imageUrl, currentUser,model);
                                    // Generate a unique key for the new object
                                    String key = plate;

                                    // Save the object to the database using the unique key
                                    databaseUsers.child(key).setValue(upload);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ParkActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ParkActivity.this, "Uploading...", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Upload upload = new Upload(plate, "No Image",currentUser,model);
            // Generate a unique key for the new object
            String key = plate;

            // Save the object to the database using the unique key
            databaseUsers.child(key).setValue(upload);
            Toast.makeText(this,"No picture inserted.",Toast.LENGTH_SHORT).show();
        }
    }
}

/*
como usar as imagens com o URL da firebase

try {
    URL url = new URL(downloadUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    InputStream inputStream = connection.getInputStream();
    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
    imageView.setImageBitmap(bitmap);
} catch (IOException e) {
    e.printStackTrace();
}

 */
