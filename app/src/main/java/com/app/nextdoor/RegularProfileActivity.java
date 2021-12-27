package com.app.nextdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class RegularProfileActivity extends AppCompatActivity {

    RegularRegistrationActivity.RegularProfile u;
    String id = "";
    String token;
    String senderName;
    String receiverName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_profile);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {

            createProfile(extras.getString("Object"));
            token = extras.getString("Key");
            senderName = extras.getString("myName");
            receiverName = extras.getString("Name");
        }
    }

    private void createProfile(String object) {
        u = null;
        try {
            byte b[] = Base64.getDecoder().decode(object.getBytes());
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            u = (RegularRegistrationActivity.RegularProfile) si.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        TextView tv1 = findViewById(R.id.LocationName2);
        TextView tv2 = findViewById(R.id.PageName2);

        assert u != null;
        tv1.append(u.address);
        tv2.append(u.fullName);
    }

    public void onClick(View v)
    {
        // The current user
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Intent i = new Intent(RegularProfileActivity.this, ChatActivity.class);
        i.putExtra("senderName", senderName);
        i.putExtra("receiverName", receiverName);
        i.putExtra("token", token);
//        setContentView(R.layout.chat_layout);
        startActivity(i);




    }

}
