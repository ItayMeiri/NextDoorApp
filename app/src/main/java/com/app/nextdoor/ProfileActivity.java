package com.app.nextdoor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {

            createProfile(extras.getString("Object"));
        }
    }

    private void createProfile(String object) {
        RegistrationActivity.RegularProfile u = null;
        try {
            byte b[] = Base64.getDecoder().decode(object.getBytes());
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            u = (RegistrationActivity.RegularProfile) si.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        TextView tv1 = findViewById(R.id.LocationName);
        TextView tv2 = findViewById(R.id.PageName);

        assert u != null;
        tv1.append(u.Address);
        tv2.append(u.FullName);
    }
}
