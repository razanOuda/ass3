package com.example.ass3_cloud;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;

     FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignup = findViewById(R.id.buttonSignup);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                signup(email, password);
            }
        });
    }

    private void signup(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(SignUp.this, "تم إنشاء الحساب بنجاح", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            Toast.makeText(SignUp.this, "كلمة المرور ضعيفة جدًا", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(SignUp.this, "البريد الإلكتروني غير صالح", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthUserCollisionException e) {
                            Toast.makeText(SignUp.this, "البريد الإلكتروني مستخدم بالفعل", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthException e) {
                            Toast.makeText(SignUp.this, "خطأ في إنشاء الحساب", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(SignUp.this, "حدث خطأ ما", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
