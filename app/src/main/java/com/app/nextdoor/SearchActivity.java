package com.app.nextdoor;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {

    //instance variables
    String City = "Tel-Aviv";

    public static class User {
        public String id;
        public String location;

        public User(String loc, String id) {
            location = loc;
            this.id = id;

        }
    }

    public void onClick(View v)
    {
        Log.i("buttonClick", "button is clicked");
        TextInputLayout TIL = findViewById(R.id.searchBar);
        City = TIL.getEditText().getText().toString();
        basicReadWrite();
    }
    public static ArrayList<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        users = new ArrayList<>();
//        basicReadWrite();


    }

    public void SearchByLocation(String location, FirebaseDatabase database)
    {

    }

    private void handleUsers(User u)
    {
        users.add(u);
    }
    public void basicReadWrite() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
//        System.out.println("test");
//        System.out.println("myref: "  + myRef.toString());
//        System.out.println("mydb: " + database.toString());

        TextView tv = findViewById(R.id.searchResults);
        tv.setText("");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("data ss: " + dataSnapshot.getValue());
                ArrayList<HashMap> gat = (ArrayList<HashMap>) dataSnapshot.getValue();
                for(HashMap map: gat)
                {
//                    System.out.println("loc=" + map.get("location"));
//                    System.out.println("query=" + map.get("location").toString().equals("Tel-Aviv"));
                    if(map.get("location").toString().equals(City))
                    {
//                        System.out.println("Entered!);
                        User u = new User(map.get("location").toString(), map.get("userId").toString());
                        handleUsers(u); // adds user
                        tv.append("user id: " + u.id + "\nuser location:" + u.location + "\n\n");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

}