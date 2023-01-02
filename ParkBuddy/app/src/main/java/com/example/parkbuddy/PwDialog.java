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
import android.widget.EditText;

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
                        if (TextUtils.isEmpty(oldpassword)){
                            OldPW.setError("Password cannot be empty");
                            OldPW.requestFocus();
                        }else if (TextUtils.isEmpty(password)) {
                            NewPW.setError("Password cannot be empty");
                            NewPW.requestFocus();
                        }else if(!password.equals(confirmPassword)){
                            NewConfirm.setError("Passwords don´t match");
                            NewConfirm.requestFocus();
                        }else {
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
                                                            
                                                            Log.d(TAG, "Password updated");
                                                        } else {
                                                            Log.d(TAG, "Error password not updated");
                                                        }
                                                    }
                                                });
                                            } else {
                                                Log.d(TAG, "Error auth failed");
                                            }
                                        }
                                    });
                        }
                    }
                });
        return builder.create();
    }
}
