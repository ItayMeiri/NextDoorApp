package com.app.nextdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

public class BusinessProfileActivity extends AppCompatActivity {

    BusinessRegistrationActivity.BusinessProfile u;
    String id = "";
    String token;
    String senderName;
    String receiverName;
    String url;
    ImageView Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            createProfile(extras.getString("Object"));
            token = extras.getString("Key");
            senderName = extras.getString("myName");
            receiverName = extras.getString("Name");
            url = extras.getString("Url");
        }

        Image = findViewById(R.id.imageView6);

    }

    private void createProfile(String object) {
        u = null;
        try {
            byte b[] = Base64.getDecoder().decode(object.getBytes());
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            u = (BusinessRegistrationActivity.BusinessProfile) si.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        TextView tv1 = findViewById(R.id.LocationName2);
        TextView tv2 = findViewById(R.id.PageName2);
        TextView tv3 = findViewById(R.id.phoneNum);
        TextView tv4 = findViewById(R.id.Slang);
        TextView tv5 = findViewById(R.id.BDescription);
        TextView tv6 = findViewById(R.id.ActivityTime);

        assert u != null;
        tv1.append(u.address);
        tv2.append(u.fullName);
        tv3.append(u.phone);
        tv6.append(u.activeTime);
        for (String i : u.serviceLang)
            tv4.append(i+ " ");
        tv5.append(u.description);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseAuth myA = FirebaseAuth.getInstance();

        db.getReference().child("users").child(myA.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Picasso.get().load(url).into(Image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onClick(View v)
    {
        // The current user
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Intent i = new Intent(BusinessProfileActivity.this, ChatActivity.class);
        i.putExtra("senderName", senderName);
        i.putExtra("receiverName", receiverName);
        i.putExtra("token", token);
//        setContentView(R.layout.chat_layout);
        startActivity(i);


    }
}