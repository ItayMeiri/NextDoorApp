package com.app.nextdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

import android.widget.TextView;

public class RegularProfileActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_profile);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {

            createProfile(extras.getString("Object"));
        }
    }

    private void createProfile(String object) {
        RegularRegistrationActivity.RegularProfile u = null;
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
        tv1.append(u.Address);
        tv2.append(u.FullName);
    }
}
