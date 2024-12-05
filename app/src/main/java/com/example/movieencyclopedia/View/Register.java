package com.example.movieencyclopedia.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieencyclopedia.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.resgister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser(binding.userNameResgister.getText().toString(), binding.passwordResgister.getText().toString());
            }
        });
    }
    private void RegisterUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot document = (DocumentSnapshot) task.getResult();
                            if (document.exists()) {
                                Log.d("Firestore", "User already exists");
                            }
                            else {
                                Log.d("tag", "signInWithEmail:success");
                                FirebaseUser registeredUser = mAuth.getCurrentUser();
                                String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

                                if (userId != null) {
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("email", email);
                                    user.put("password", password);

                                    db.collection("users").document(userId).set(user)
                                            .addOnSuccessListener(aVoid -> {
                                                Log.d("Firestore", "User added successfully");
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.w("Firestore", "Error adding user", e);
                                            });
                                    Toast.makeText(Register.this, "Register User Passed", Toast.LENGTH_SHORT).show();
                                    Intent intentObj = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intentObj);
                                    finish();
                                }
                            }
                        }else{
                            Toast.makeText(Register.this, "Register User Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}