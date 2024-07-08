package com.temmahadi.ecoroot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class DetailPage extends AppCompatActivity {
    TextView title, weight, desc;
    ImageView img;
    String imgUrl, node;
    FloatingActionButton delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        title = findViewById(R.id.detailTitle);
        weight = findViewById(R.id.detailWeight);
        desc = findViewById(R.id.detailDesc);
        img = findViewById(R.id.detailImage);
        delete = findViewById(R.id.delete);

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String USERNAME = sharedPref.getString("username", null);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            desc.setText(String.format("Description: %s", bundle.getString("Description")));
            title.setText(bundle.getString("Title"));
            weight.setText(String.format("Weight: %s gm", bundle.getString("Weight")));
            node = bundle.getString("Title");
            imgUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(img);
        }

        AlertDialog.Builder builder= new AlertDialog.Builder(DetailPage.this);
        builder.setIcon(R.drawable.baseline_delete_24);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog dialog = progressBar();
                dialog.show();

                DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Usage").child(USERNAME).child(node);
                FirebaseStorage.getInstance().getReferenceFromUrl(imgUrl).delete()
                        .addOnSuccessListener(aVoid -> {
                            dr.removeValue()
                                    .addOnSuccessListener(aVoid1 -> {
                                        dialog.dismiss();
                                        Toast.makeText(DetailPage.this, "Deleted", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(DetailPage.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(DetailPage.this, "Failed to delete data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    });
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(DetailPage.this, "Failed to delete image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Delete!");
                builder.setMessage("Do you want to delete this item?");
                builder.show();
            }
        });
    }

    public AlertDialog progressBar(){
        AlertDialog.Builder builder= new AlertDialog.Builder(DetailPage.this);
        builder.setView(R.layout.progress_bar);
        builder.setCancelable(false);
        return builder.create();
    }
}
