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

public class EmailDialog extends AppCompatDialogFragment {

    TextInputEditText PW, NewMail;
    FirebaseUser user;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialogmail, null);


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
                        PW = view.findViewById(R.id.etPW);
                        NewMail = view.findViewById(R.id.etNewMail);
                        user = FirebaseAuth.getInstance().getCurrentUser();

                        String password = PW.getText().toString();
                        String mail = NewMail.getText().toString();
                        if (TextUtils.isEmpty(password)){
                            Toast.makeText(getActivity(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                            PW.setError("Password cannot be empty");
                            PW.requestFocus();
                        }else if (TextUtils.isEmpty(mail)) {
                            Toast.makeText(getActivity(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
                            NewMail.setError("Email cannot be empty");
                            NewMail.requestFocus();
                        }else {
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(user.getEmail(), password);

// Prompt the user to re-provide their sign-in credentials
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                user.updateEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getActivity(), "Mail Updated", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "Password updated");
                                                        } else {
                                                            Toast.makeText(getActivity(), "Error updating email", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "Error password not updated");
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(getActivity(), "Error updating email", Toast.LENGTH_SHORT).show();
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
