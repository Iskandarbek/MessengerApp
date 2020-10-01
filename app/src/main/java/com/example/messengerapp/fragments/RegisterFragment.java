package com.example.messengerapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.messengerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;


public class RegisterFragment extends Fragment {

    MaterialEditText username, email, password;
    Button btn_register;

    FirebaseAuth auth; // для авторизации
    DatabaseReference reference; // для указании место хранения объекта в БД


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        btn_register = view.findViewById(R.id.register_btn);

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(getContext(),"Fill in all fields", Toast.LENGTH_LONG).show();
                }
                else if (txt_password.length() < 6){
                    Toast.makeText(getContext(),"Your password should contain minimum 6 characters", Toast.LENGTH_LONG).show();
                }
                else {
                    register(txt_username,txt_email,txt_password);
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
    public void register(final String username, final String email, String password){

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            String userId = user.getUid();

                            reference = FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(userId);

                            HashMap<String, String> map = new HashMap<>();
                            map.put("id", userId);
                            map.put("username", username);
                            map.put("email", email);

                            reference.setValue(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getContext(), "Registration successfully copmleted", Toast.LENGTH_LONG).show();
                                                ViewPager layout = (ViewPager) getActivity().findViewById(R.id.view_pager);
                                                layout.setCurrentItem(0);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}