package com.example.ass3_cloud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    private ImageView profileImage;
    private EditText editName;
    private EditText editPhone;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileImage = findViewById(R.id.profile_image);
        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        btnSave = findViewById(R.id.btn_save);

        SharedPreferences sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String name = sharedPref.getString("name", "");
        String phone = sharedPref.getString("phone", "");

        editName.setText(name);
        editPhone.setText(phone);

        sendNotificationToProfileTopic();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });

        loadProfileData();
    }

    private void loadProfileData() {

        String name = "John Doe";
        String phone = "0123456789";
        editName.setText(name);
        editPhone.setText(phone);
    }

    private void saveProfileData() {


        String newName = editName.getText().toString().trim();
        String newPhone = editPhone.getText().toString().trim();



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection("YOUR_COLLECTION_PATH").document("documentId");

        documentRef.update("name", newName, "phone", newPhone)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "تم حفظ البيانات بنجاح", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "حدث خطأ أثناء حفظ البيانات", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendNotificationToProfileTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("profile")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("FCM", "تم الاشتراك في توبيك profile بنجاح");
                        } else {
                            Log.d("FCM", "فشل في الاشتراك في توبيك profile");
                        }
                    }
                });
    }
}