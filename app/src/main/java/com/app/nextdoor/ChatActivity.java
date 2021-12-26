package com.app.nextdoor;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String otherUser = ""; // Mike Mikey - just to test!
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    DatabaseReference ref;
    String senderName;
    String receiverName;

    public static class Message
    {
        Date date;
        String msg;
        String senderID;
        String receiverID;
        boolean seen;

        public Message()
        {}

        public Message(Date date,  String msg,String senderID, String receiverID, boolean seen)
        {
            this.date = date;
            this.senderID = senderID;
            this.receiverID = receiverID;
            this.msg = msg;
            this.seen = seen;
        }

        public Date getDate()
        {
            return date;
        }
        public String getMsg()
        {
            return msg;
        }
        public String getSenderID(){return senderID;}
        public String getReceiverID(){return receiverID;}
        public boolean getSeen()
        {
            return seen;
        }

        public String toString()
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            return getSenderID() + "(" + date.getHours() +":" + date.getMinutes() + ")" + ":" + msg;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null)
        {
            setContentView(R.layout.activity_login);
        }
        else
        {

            setContentView(R.layout.chat_layout);
            Bundle extras = getIntent().getExtras();
            otherUser = extras.getString("token");
            senderName = extras.getString("senderName");
            receiverName = extras.getString("receiverName");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            if(mAuth.getCurrentUser().getUid().compareTo(otherUser) > 0)
            {
                ref = database.getReference("chat/" + mAuth.getCurrentUser().getUid() + "/" + otherUser);
            }
            else
            {
                ref = database.getReference("chat/" + otherUser + "/" + mAuth.getCurrentUser().getUid());
            }

            DatabaseReference userRef = database.getReference("users/Regular/" + otherUser);
            listItems=new ArrayList<String>();
            setView();
        }
    }


    // Method responsible for setting up the view from the database
    private void setView() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Combine users to form a unique chat session id. This will remain consistent across users because FirebaseID is consistent.

        ArrayList<Message> messages = new ArrayList<>();
        ListView listView = findViewById(R.id.MessagesListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        ValueEventListener postListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
//                listView.setAdapter(null);
                adapter.clear();
                for(DataSnapshot snap : dataSnapshot.getChildren())
                {
                    Message msg = snap.getValue(Message.class);
                    adapter.add(msg.toString());
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("1", "loadPost:onCancelled", databaseError.toException());
            }
        };

        ref.addValueEventListener(postListener);
    }


    public void onClick(View v)
    {
        //get input msg
        TextInputLayout TIL = findViewById(R.id.MessageInput);
        String msgToSend = TIL.getEditText().getText().toString();
        Date currentTime = Calendar.getInstance().getTime();
        String senderID = mAuth.getCurrentUser().getUid();
        String receiverID = otherUser;


        //check if msg is not empty
        if(msgToSend.isEmpty())
        {
            return; // can't send empty msg!
        }

        //maybe add cool-down?

//        Message message = new Message(currentTime, msgToSend,mAuth.getCurrentUser().getUid(), otherUser, false);
        Message message = new Message(currentTime, msgToSend, senderName, receiverName, false);
        ref.push().setValue(message);
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                adapter.add(message.toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w("2", "loadPost:onCancelled", error.toException());
//            }
//        });
        System.out.println("ref: " + ref);

        //clear input
        TIL.getEditText().setText("");
    }
}
