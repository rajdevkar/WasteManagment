package com.wastemanagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wastemanagment.adapters.listAdapter;
import com.wastemanagment.models.User;

import java.util.ArrayList;
import java.util.List;

public class AgentActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser curUser;

    String uid;
    Button logout_btn, refresh_btn;
    ProgressBar loader;

    List<User> List = new ArrayList<>();
    private RecyclerView recyclerView;
    private listAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        curUser = auth.getCurrentUser();

        if (curUser != null)
            uid = curUser.getUid();

        loader = findViewById(R.id.loader);
        logout_btn = findViewById(R.id.logout);
        refresh_btn = findViewById(R.id.refresh);
        recyclerView = findViewById(R.id.recyclerView);

        loader.setVisibility(View.GONE);

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getList();
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(AgentActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mAdapter = new listAdapter(List);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        getList();
    }

    private void getList() {
        loader.setVisibility(View.VISIBLE);
        List.clear();
        mAdapter.notifyDataSetChanged();
        db.collection("Users")
                .whereEqualTo("available", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = new User(document.get("username").toString(), document.get("phone").toString());
                                List.add(user);
                                mAdapter.notifyDataSetChanged();
                            }
                            loader.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
