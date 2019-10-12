package com.wastemanagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wastemanagment.models.User;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    FirebaseFirestore db;

    EditText et_email, et_password;
    String uid, email, password, str_userType;
    TextView account;
    Button btn_login;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait.");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.register_btn);
        account = findViewById(R.id.account);

        et_email.addTextChangedListener(TextWatcher);
        et_password.addTextChangedListener(TextWatcher);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    dialog.dismiss();
                                    if (password.length() < 6) {
                                        Toast.makeText(LoginActivity.this, "password length short", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    FirebaseUser curUser = auth.getInstance().getCurrentUser();
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
                                                            str_userType = document.get("userType").toString();
                                                        }
                                                        if (str_userType.equals("User"))
                                                            startActivity(new Intent(LoginActivity.this, UserActivity.class));
                                                        else if (str_userType.equals("Agent"))
                                                            startActivity(new Intent(LoginActivity.this, AgentActivity.class));
                                                        dialog.dismiss();
                                                        finish();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        });
    }

    private android.text.TextWatcher TextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            email = et_email.getText().toString().trim();
            password = et_password.getText().toString().trim();

            if (!email.isEmpty() && email.matches(emailPattern) && !password.isEmpty() && password.length() > 7)
                btn_login.setEnabled(true);
            else
                btn_login.setEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
