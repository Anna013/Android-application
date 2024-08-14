package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private TextView haveAcount, rbutton;
    private EditText email, pass, username;
    private FirebaseAuth ff;
    private DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        haveAcount = (TextView) findViewById(R.id.have_acount);
        haveAcount.setOnClickListener(this);

        rbutton = (Button) findViewById(R.id.register);
        rbutton.setOnClickListener(this);
        email = (EditText) findViewById(R.id.email2);
        username = (EditText) findViewById(R.id.usernameReg);
        pass = (EditText) findViewById(R.id.password);
        ff = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.have_acount:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.register:
                register();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

    private void register() {
        String userEmail = email.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String name = username.getText().toString().trim();

        if (userEmail.isEmpty()) {
            email.setError("email is required!");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.setError("email is not valid!");
            email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            pass.setError("password is required!");
            pass.requestFocus();
            return;
        }
        if (name.isEmpty()) {
            username.setError("password is required!");
            username.requestFocus();
            return;
        }
        if (password.length() < 4) {
            pass.setError("password length should be 4 characters!");
            pass.requestFocus();
            return;
        }

        ff.createUserWithEmailAndPassword(userEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String uid = ff.getCurrentUser().getUid();
                    System.out.println("uid  " + uid);
                    dbref.child(uid).child("email").setValue(userEmail);
                    dbref.child(uid).child("password").setValue(password);
                    dbref.child(uid).child("username").setValue(name);
                    Toast.makeText(Registration.this, "successfull registration", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(Registration.this, "registration error", Toast.LENGTH_LONG).show();
            }
        });

    }



}