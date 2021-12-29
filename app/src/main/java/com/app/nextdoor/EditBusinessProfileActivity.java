package com.app.nextdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class EditBusinessProfileActivity extends AppCompatActivity {

    Button update;
    EditText fullName;
    EditText activityTime;
    EditText description;
    EditText serviceLang;
    EditText address;
    EditText phone;
    TextView plus;
    ArrayList Sl = new ArrayList();
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth myA;
    HashMap hm = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_business_profile);

        update = findViewById(R.id.button7);
        fullName = findViewById(R.id.editTextTextPersonName13);
        address = findViewById(R.id.editTextTextPersonName6);
        activityTime = findViewById(R.id.editTextTextPersonName7);
        phone = findViewById(R.id.editTextTextPersonName8);
        description = findViewById(R.id.editTextTextPersonName9);
        serviceLang = findViewById(R.id.editTextTextPersonName10);
        plus = findViewById(R.id.textView23);
        Sl.add("");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        myA = FirebaseAuth.getInstance();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                reference.child("users").child("Bussiness").child(myA.getUid()).updateChildren(hm);
            }
        } );

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!serviceLang.getText().toString().equals("")) {
                    addLang();
                }
            }
        });
    }

    public void addLang(){
        Sl.add(serviceLang.getText().toString());
        serviceLang.getText().clear();
        hm.put("serviceLang", Sl);
    }

}

