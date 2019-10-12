package com.wastemanagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wastemanagment.models.User;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth auth;
    FirebaseFirestore db;

    EditText et_username, et_email, et_password, et_phone;
    String uid, username, email, password, phone;
    TextView gotologin;
    Button btn_register;
    ImageButton back_btn;
    Spinner userType;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String str_userType = "User";
    String userTypeArray[] = {"User", "Agent"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait.");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_phone = findViewById(R.id.et_phone);
        btn_register = findViewById(R.id.register_btn);
        gotologin = findViewById(R.id.gotologin);
        back_btn = findViewById(R.id.back_btn);

        userType = findViewById(R.id.userType);
        userType.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypeArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userType.setAdapter(aa);

        et_username.addTextChangedListener(TextWatcher);
        et_email.addTextChangedListener(TextWatcher);
        et_password.addTextChangedListener(TextWatcher);
        et_phone.addTextChangedListener(TextWatcher);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Try again later",
                                            Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    FirebaseUser curUser = auth.getInstance().getCurrentUser();
                                    if (curUser != null)
                                        uid = curUser.getUid();
                                    User user = new User(
                                            uid,
                                            username,
                                            email,
                                            phone,
                                            str_userType,
                                            false
                                    );
                                    db.collection("Users")
                                            .document(uid)
                                            .set(user);
                                    if (str_userType == "User") {
                                        startActivity(new Intent(RegisterActivity.this, UserActivity.class));
                                    } else if (str_userType == "Agent") {
                                        startActivity(new Intent(RegisterActivity.this, AgentActivity.class));
                                    }
                                    finish();
                                    dialog.dismiss();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        str_userType = userTypeArray[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        str_userType = "User";
    }

    private TextWatcher TextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            username = et_username.getText().toString().trim();
            email = et_email.getText().toString().trim();
            password = et_password.getText().toString().trim();
            phone = et_phone.getText().toString().trim();

            if (!username.isEmpty() && !email.isEmpty() && email.matches(emailPattern) && !password.isEmpty() && password.length() > 7 && !phone.isEmpty())
                btn_register.setEnabled(true);
            else
                btn_register.setEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
