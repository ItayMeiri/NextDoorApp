package com.app.nextdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class RegularProfileActivity extends AppCompatActivity {

    RegularRegistrationActivity.RegularProfile u;
    String id = "";
    String token;
    String senderName;
    String receiverName;
    ImageView Image;
    FirebaseStorage storage;
    StorageReference reference;
    FirebaseAuth A;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            createProfile(extras.getString("Object"));
            token = extras.getString("Key");
            senderName = extras.getString("myName");
            receiverName = extras.getString("Name");
        }

        A = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        Image = findViewById(R.id.imageView6);
        reference = storage.getReference("users").child(A.getUid());
        try {
            File file = File.createTempFile("images","jpg");
            reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Image.setImageURI(Uri.fromFile(file));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("hii");
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
        TextView tv3 = findViewById(R.id.Ageee);
        TextView tv4 = findViewById(R.id.phoneNum);
        TextView tv5 = findViewById(R.id.ActivityTime);
        TextView tv6 = findViewById(R.id.Slang);
        TextView tv7 = findViewById(R.id.BDescription);

        assert u != null;
        tv1.append(u.address);
        tv2.append(u.fullName);
        tv3.append(u.age);
        tv4.append(u.phone);
        tv5.append(u.job);
        for (String i : u.lang)
            tv6.append(i+ " ");
        for (String i : u.hobbies)
            tv7.append(i+ " ");
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
