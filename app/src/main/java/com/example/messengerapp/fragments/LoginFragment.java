package com.example.messengerapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.messengerapp.MainActivity;
import com.example.messengerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;


public class LoginFragment extends Fragment {

    MaterialEditText email, password;
    Button login_btn;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login,container,false);

        email = view.findViewById(R.id.email_l);
        password = view.findViewById(R.id.password_l);
        auth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        login_btn = view.findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(getContext(),"All fields should be filled", Toast.LENGTH_LONG).show();
                }
                else {
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        progressBar.setVisibility(View.GONE);
                                        Log.i("TAG", "onComplete");
                                        Toast.makeText(getContext(),"You've successfully logged in",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                    }
                                    else {
                                        Log.i("TAG", task.toString());
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT);
                                }
                            });
                }
            }
        });

        return inflater.inflate(R.layout.fragment_login, container, false);
    }
}