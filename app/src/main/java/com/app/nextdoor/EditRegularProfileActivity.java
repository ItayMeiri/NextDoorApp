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
    EditText address;
    EditText phone;
    EditText languages;
    EditText Hobbies;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth myA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_regular_profile);

        update = findViewById(R.id.button4);
        name = findViewById(R.id.editTextTextPersonName);
        address = findViewById(R.id.editTextTextPersonName2);
        age = findViewById(R.id.editTextTextPersonName3);
        phone = findViewById(R.id.editTextTextPersonName4);
        job = findViewById(R.id.editTextTextPersonName5);
        languages = findViewById(R.id.editText);
        Hobbies = findViewById(R.id.editText2);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        myA = FirebaseAuth.getInstance();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap hm = new HashMap<>();

                if (!name.getText().toString().equals("")){
                    hm.put("fullName",name.getText().toString());
                }
                if (!address.getText().toString().equals("")){
                    hm.put("address",address.getText().toString());
                }
                if (!age.getText().toString().equals("")){
                    hm.put("age",age.getText().toString());
                }
                if (!job.getText().toString().equals("")){
                    hm.put("job",job.getText().toString());
                }
                if (!phone.getText().toString().equals("")){
                    hm.put("phone",phone.getText().toString());
                }
                if (!languages.getText().toString().equals("")){
                    hm.put("lang",languages.getText().toString());
                }
                if (!Hobbies.getText().toString().equals("")){
                    hm.put("hobbies",Hobbies.getText().toString());
                }
                reference.child("users").child("Regular").child(myA.getUid()).updateChildren(hm);
            }
        });

    }
}
