package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText pass, email;
    private Button button;
    private FirebaseAuth ff;
    Context context;
    Resources resources;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference dbref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://auth-94ef9-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = findViewById(R.id.register);
        email = findViewById(R.id.userLog);
        pass = findViewById(R.id.password);
        button = findViewById(R.id.LogIn);
        ff = FirebaseAuth.getInstance();

        if (ff.getCurrentUser() != null) {
            startActivity(new Intent(this, MovieShow.class));
            finish();

        }

        register.setOnClickListener(this);
        button.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(this, Registration.class));
                finish();
                break;
            case R.id.LogIn:
                checkLogin();
                break;
        }
    }

    public void checkLogin() {

        String password = pass.getText().toString();
        String userEmail = email.getText().toString();

        if (userEmail.isEmpty()) {
            email.setError("email is required!");
            email.requestFocus();

        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.setError("email is not valid!");
            email.requestFocus();

        } else if (password.isEmpty()) {
            pass.setError("password is required!");
            pass.requestFocus();

        } else if (password.length() < 4) {
            pass.setError("password length should be 4 characters!");
            pass.requestFocus();

        } else {


            ff.signInWithEmailAndPassword(userEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(MainActivity.this, MovieShow.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "login error", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }

    }










}