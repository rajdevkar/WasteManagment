package com.wastemanagment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Splash extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    String uid, userType;
    FirebaseUser curUser;
    private FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        curUser = auth.getCurrentUser();

        if (curUser != null)
            uid = curUser.getUid();

        db.collection("Users")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userType = document.get("userType").toString();
                            }
                            if (curUser != null) {
                                if (userType.equals("User"))
                                    startActivity(new Intent(Splash.this, UserActivity.class));
                                else if (userType.equals("Agent"))
                                    startActivity(new Intent(Splash.this, AgentActivity.class));
                            } else {
                                startActivity(new Intent(Splash.this, LoginActivity.class));
                            }
                            finish();
                        }
                    }
                });
    }
}