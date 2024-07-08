package com.temmahadi.ecoroot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUpPage extends AppCompatActivity {
EditText name,username,email,address,password;
TextView login;
Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        name= findViewById(R.id.sign_name);
        username = findViewById(R.id.sign_username);
        email= findViewById(R.id.sign_email);
        address= findViewById(R.id.sign_address);
        password= findViewById(R.id.sign_password);
        login= findViewById(R.id.loginRedirectText);
        register= findViewById(R.id.sign_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n,u,e,a,p;
                n= name.getText().toString();
                u= username.getText().toString();
                e= email.getText().toString();
                a= address.getText().toString();
                p= password.getText().toString();

            DatabaseReference dr= FirebaseDatabase.getInstance().getReference("Users");
            Query check= dr.orderByChild("username").equalTo(u);
            check.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        LoginData loginData= new LoginData(n,u,e,a,p);
                        dr.child(u).setValue(loginData);

                        Toast.makeText(SignUpPage.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                        startActivity(intent);
                        finish();
                    }else {
                        username.setError("username exist");
                        username.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });

    }
}