package com.example.ass3_cloud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        SharedPreferences sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", String.valueOf(editTextEmail));
        editor.apply();

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                signin(email, password);
            }
        });
    }

    private void signin(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(SignIn.this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            Toast.makeText(SignIn.this, "البريد الإلكتروني غير مسجل", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(SignIn.this, "بيانات الاعتماد غير صالحة", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(SignIn.this, "حدث خطأ ما", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}