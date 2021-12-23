package com.app.nextdoor;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import java.util.Base64;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity{

    //instance variables

    String nameToSearch = "";
    String cityToSearch = "";
    ArrayList<RegistrationActivity.RegularProfile> users;

    public void onClick(View v)
    {
        Log.i("buttonClick", "button is clicked");

        TextInputLayout input = findViewById(R.id.CityInput);
        TextInputLayout NameInput= findViewById(R.id.NameInput);

        nameToSearch = NameInput.getEditText().getText().toString();
        cityToSearch = input.getEditText().getText().toString();
        CheckBox cb = findViewById(R.id.checkBox);
        if(cb.isChecked())
        {
            ListAllItems("Business",cb);
        }
        else
        {
            ListAllItems("Regular",cb);
        }

//        basicReadWrite();
    }

    private void ListAllItems(String type,CheckBox cb) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + type);
        addListener(ref,cb);
    }

    private void addListener(DatabaseReference ref,CheckBox cb)
    {

        ArrayList<String> listItems=new ArrayList<String>();
        users = new ArrayList<>();
//        listItems.add("test2");


        // get list view
        ListView listView = findViewById(R.id.ListView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(SearchActivity.this, RegularProfileActivity.class);
                    RegistrationActivity.RegularProfile rp = users.get(i);
                    String serializedObject = "";
                    try {
                        ByteArrayOutputStream bo = new ByteArrayOutputStream();
                        ObjectOutputStream so = new ObjectOutputStream(bo);
                        so.writeObject(rp);
                        so.flush();
                        serializedObject = new String(Base64.getEncoder().encode(bo.toByteArray()));
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    intent.putExtra("Object", serializedObject);
                    startActivity(intent);
                }
        });
        ValueEventListener postListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
//                adapter.add("test3");
//                System.out.println("dsn: " + dataSnapshot.toString());

                for(DataSnapshot snap : dataSnapshot.getChildren())
                {
                    RegistrationActivity.RegularProfile RP = snap.getValue(RegistrationActivity.RegularProfile.class);
                    if(RP.Address.contains(cityToSearch) && RP.FullName.contains(nameToSearch))
                    {
//                        listItems.add(RP.toString());
                        users.add(RP);
                        adapter.add(RP.toString());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("1", "loadPost:onCancelled", databaseError.toException());
            }
        };

        ref.addValueEventListener(postListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
    }

//    public void basicReadWrite() {
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("users");
////        System.out.println("test");
//        System.out.println("myref: "  + myRef.toString());
//        System.out.println("mydb: " + database.toString());
//
//        TextView tv = findViewById(R.id.searchResults);
//        tv.setText("");
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                System.out.println("data ss: " + dataSnapshot.getValue());
//                ArrayList<HashMap> gat = (ArrayList<HashMap>) dataSnapshot.getValue();
//                for(HashMap map: gat)
//                {
////                    System.out.println("loc=" + map.get("location"));
////                    System.out.println("query=" + map.get("location").toString().equals("Tel-Aviv"));
//                    if(map.get("location").toString().equals(City))
//                    {
////                        System.out.println("Entered!);
//                        User u = new User(map.get("location").toString(), map.get("userId").toString());
//                        handleUsers(u); // adds user
//                        tv.append("user id: " + u.id + "\nuser location:" + u.location + "\n\n");
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//            }
//        });
//        //write test
//
//    }

}