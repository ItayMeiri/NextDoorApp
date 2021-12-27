package com.app.nextdoor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseChatActivity extends AppCompatActivity {

    String senderName = "null";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_chatroom);
        populateList();
    }

    private void populateList() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/Regular/");
        ListView myFriends = findViewById(R.id.FriendsList);
        ArrayList<String> listItems = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<RegularRegistrationActivity.RegularProfile> users = new ArrayList<>();
        myFriends.setAdapter(adapter);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren())
                {

                    RegularRegistrationActivity.RegularProfile RP = snap.getValue(RegularRegistrationActivity.RegularProfile.class);
                    if(snap.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        senderName = RP.FullName;
                        System.out.println("UPDATED! " + senderName);
                        continue;
                    }
                    ids.add(snap.getKey());
                    adapter.add(RP.toString());
                    users.add(RP);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("ERROR: oncancelled");
            }
        });

        myFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChooseChatActivity.this, ChatActivity.class);
                String receiverName = users.get(i).FullName;
                String token = ids.get(i);
                intent.putExtra("token", token);
                intent.putExtra("receiverName",receiverName);
                intent.putExtra("senderName", senderName);
                startActivity(intent);
            }
        });

    }


}
