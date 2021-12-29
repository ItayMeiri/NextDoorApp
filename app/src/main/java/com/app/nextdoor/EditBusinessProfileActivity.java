package com.app.nextdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditBusinessProfileActivity extends AppCompatActivity {

    Button update;
    EditText fullName;
    EditText activityTime;
    EditText description;
    EditText serviceLang;
    EditText address;
    EditText phone;
    EditText url;
    EditText imageurll;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth myA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_business_profile);
        Button update;
        EditText fullName;
        EditText address;
        EditText activityTime;
        EditText phone;
        EditText description;
        EditText serviceLang;
        EditText url;
        EditText imageurll;
        FirebaseDatabase database;
        DatabaseReference reference;
        FirebaseAuth myA;

        update = findViewById(R.id.button7);
        fullName = findViewById(R.id.editTextTextPersonName5);
        address = findViewById(R.id.editTextTextPersonName6);
        activityTime = findViewById(R.id.editTextTextPersonName7);
        phone = findViewById(R.id.editTextTextPersonName8);
        description = findViewById(R.id.editTextTextPersonName9);
        serviceLang = findViewById(R.id.editTextTextPersonName10);
        url= findViewById(R.id.editTextTextPersonName11);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        myA = FirebaseAuth.getInstance();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap hm = new HashMap<>();

                if (!fullName.getText().toString().equals("")) {
                    hm.put("fullName", fullName.getText().toString());
                }
                if (!address.getText().toString().equals("")) {
                    hm.put("address", address.getText().toString());
                }
                if (!activityTime.getText().toString().equals("")) {
                    hm.put("activityTime", activityTime.getText().toString());
                }
                if (!phone.getText().toString().equals("")) {
                    hm.put("phone", phone.getText().toString());
                }
                if (!description.getText().toString().equals("")) {
                    hm.put("description", description.getText().toString());
                }
                if (!serviceLang.getText().toString().equals("")) {
                    hm.put("serviceLang", serviceLang.getText().toString());
                }
                if (!url.getText().toString().equals("")) {
                    hm.put("url", url.getText().toString());
                }
                reference.child("users").child("Bussiness").child(myA.getUid()).updateChildren(hm);
            }
        } );
    }

}

