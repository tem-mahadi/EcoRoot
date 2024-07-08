package com.temmahadi.ecoroot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginPage extends AppCompatActivity {
EditText username,password;
Button login;
TextView signup;
String u,p; SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (sharedPref.getBoolean("next", false)) {
            // If the user is already logged in, start MainActivity and finish LoginActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return; // Exit onCreate to prevent further execution
        }

        username= findViewById(R.id.login_username);
        password= findViewById(R.id.login_password);
        login= findViewById(R.id.login_button);
        signup= findViewById(R.id.signupRedirectText);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {

                } else {
                    checkUser();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this,SignUpPage.class));
                finish();
            }
        });
    }

    public Boolean validateUsername() {
        u = username.getText().toString();
        if (u.isEmpty()) {
            username.setError("Username cannot be empty");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        p = password.getText().toString();
        if (p.isEmpty()) {
            password.setError("Password cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }
    public void checkUser(){
        DatabaseReference dr= FirebaseDatabase.getInstance().getReference("Users");
        Query check = dr.orderByChild("username").equalTo(u);

        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    username.setError(null);
                    String pDB= snapshot.child(u).child("password").getValue(String.class);
                    if(Objects.equals(pDB, p)){
                        String name = snapshot.child(u).child("name").getValue(String.class);
                        String email = snapshot.child(u).child("email").getValue(String.class);
                        String address = snapshot.child(u).child("address").getValue(String.class);

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("username", u);
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.putString("address", address);
                        editor.putBoolean("next", true);
                        editor.apply();
                        Intent intent= new Intent(LoginPage.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        password.setError("Invalid Credentials");
                        password.requestFocus();
                    }
                }
                else {
                    username.setError("User does not exist");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}