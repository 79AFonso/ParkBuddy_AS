package com.example.parkbuddy;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PwDialog extends AppCompatDialogFragment {

    TextInputEditText OldPW, NewPW, NewConfirm;
    FirebaseUser user;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialogpw, null);


        builder.setView(view)
                .setTitle("Change Password")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OldPW = view.findViewById(R.id.etOldPass);
                        NewPW = view.findViewById(R.id.etNewPass);
                        NewConfirm = view.findViewById(R.id.etNewConfirmPass);
                        user = FirebaseAuth.getInstance().getCurrentUser();

                        String oldpassword = OldPW.getText().toString();
                        String password = NewPW.getText().toString();
                        String confirmPassword = NewConfirm.getText().toString();

                        Log.d(TAG, "Old password: " + oldpassword);

                        if (TextUtils.isEmpty(oldpassword)){
                            builder.setCancelable(false);
                            Toast.makeText(getActivity(), "Error Old Password is empty", Toast.LENGTH_SHORT).show();
                        }else if (password.isEmpty()) {
                            builder.setCancelable(false);
                            Toast.makeText(getActivity(), "New Password is empty", Toast.LENGTH_SHORT).show();
                        }else if(!password.equals(confirmPassword)){
                            builder.setCancelable(false);
                            Toast.makeText(getActivity(), "Passwords dont match", Toast.LENGTH_SHORT).show();
                        }else {
                            builder.setCancelable(true);
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(user.getEmail(), oldpassword);

                            // Prompt the user to re-provide their sign-in credentials
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getActivity(), "Password updated", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "Password updated");
                                                        } else {
                                                            Toast.makeText(getActivity(), "Error password not updated", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "Error password not updated");
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(getActivity(), "Error updating password", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "Error auth failed");
                                            }
                                        }
                                    });
                        }
                    }
                });
        builder.setCancelable(false);
        return builder.create();
    }
}
