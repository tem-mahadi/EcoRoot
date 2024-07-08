package com.temmahadi.ecoroot;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadData extends AppCompatActivity {
EditText title,desc,weight;
ImageView image; Button save;
String imgUrl; Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        String USERNAME = getIntent().getExtras().getString("username");
        title = findViewById(R.id.uploadTitle);
        desc = findViewById(R.id.uploadDesc);
        image = findViewById(R.id.uploadImage);
        save = findViewById(R.id.saveButton);
        weight = findViewById(R.id.uploadweight);

        ActivityResultLauncher<Intent> activityResultLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if(o.getResultCode()== Activity.RESULT_OK){
                    uri= o.getData().getData();
                    image.setImageURI(uri);
                }else
                    Toast.makeText(UploadData.this,"No Image Selected",Toast.LENGTH_SHORT).show();
            }
                }
        );

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imgPicker= new Intent(Intent.ACTION_PICK);
                imgPicker.setType("image/*");
                activityResultLauncher.launch(imgPicker);
            }
        });

        AlertDialog.Builder builder= new AlertDialog.Builder(UploadData.this);
        builder.setIcon(R.drawable.baseline_save_alt_24);
        builder.setTitle("Save!");
        builder.setMessage("Do you want to save?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String t = title.getText().toString();
                String d = desc.getText().toString();
                String w = weight.getText().toString();
                if (!t.isEmpty() && !d.isEmpty() && !w.isEmpty() && uri != null) {
                    AlertDialog progressDialog = progressBar();
                    progressDialog.show();
                    saveImage(t, d, w, progressDialog);
                } else {
                    Toast.makeText(UploadData.this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
                }
            }

            private void saveImage(String t, String d, String w, AlertDialog progressDialog) {
                StorageReference sr = FirebaseStorage.getInstance().getReference().child(USERNAME).child(uri.getLastPathSegment());
                sr.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                    task.addOnSuccessListener(uriImage -> {
                        String imgUrl = uriImage.toString();
                        float weight = Float.parseFloat(w);
                        uploadData(t, d, weight, imgUrl, progressDialog);
                    });
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(UploadData.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            private void uploadData(String t, String d, float w, String imgUrl, AlertDialog progressDialog) {
                UserData userData = new UserData(t, d, w, imgUrl);
                FirebaseDatabase.getInstance().getReference("Usage").child(USERNAME).child(t)
                        .setValue(userData).addOnCompleteListener(task -> {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(UploadData.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UploadData.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(UploadData.this, "Failed to save data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(UploadData.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());

        save.setOnClickListener(view -> builder.show());

    }

    public AlertDialog progressBar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadData.this);
        builder.setView(R.layout.progress_bar);
        builder.setCancelable(false);
        return builder.create();
    }
}