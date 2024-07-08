package com.temmahadi.ecoroot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout; Toolbar toolbar;
    NavigationView navigationView;
    String USERNAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar= findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        USERNAME = sharedPref.getString("username", null);
        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer, R.string.close_drawer); // Pass actionBar instead of null
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        loadFragment(frag_home.newInstance(USERNAME,"ok"));

        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.drawable.baseline_logout_24);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(MainActivity.this,LoginPage.class));
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this,"Welcome Back!",Toast.LENGTH_SHORT).show();
            }
        });

        navigationView= findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id==R.id.home){
                    loadFragment(frag_home.newInstance(USERNAME,"ok"));
                }
                else if(id==R.id.profile){
                    loadFragment(new frag_profile());
                }
                else{
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("next", false);
                    editor.apply();
                    builder.setTitle("Log Out!");
                    builder.setMessage("Do you want to log out?");
                    builder.show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else super.onBackPressed();
    }
}