package com.app.nextdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Objects;

public class EditRegularProfileActivity extends AppCompatActivity {

    Button update;
    EditText name;
    EditText job;
    EditText age;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth myA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_regular_profile);

        update = findViewById(R.id.button4);
        name = findViewById(R.id.editTextTextPersonName);
        job = findViewById(R.id.editTextTextPersonName2);
        age = findViewById(R.id.editTextTextPersonName3);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        myA = FirebaseAuth.getInstance();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap hm = new HashMap<>();
                hm.put("fullName",name.getText().toString());
                hm.put("fullName",name.getText().toString());
                hm.put("fullName",name.getText().toString());
                hm.put("fullName",name.getText().toString());
                hm.put("fullName",name.getText().toString());
                hm.put("fullName",name.getText().toString());
                reference.child("users").child("Regular").child(myA.getUid()).updateChildren(hm);
                reference.child("users").child("Regular").child(myA.getUid()).updateChildren(hm);
                reference.child("users").child("Regular").child(myA.getUid()).updateChildren(hm);
                reference.child("users").child("Regular").child(myA.getUid()).updateChildren(hm);
                reference.child("users").child("Regular").child(myA.getUid()).updateChildren(hm);
                reference.child("users").child("Regular").child(myA.getUid()).updateChildren(hm);
            }
        });

    }
}
