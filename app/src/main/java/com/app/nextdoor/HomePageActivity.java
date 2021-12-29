package com.app.nextdoor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomePageActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    Button search;
    Button menu;
    Spinner sp;
    ArrayAdapter<String> adapter;
    String userType;
    String latest_post = "";
    NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        notificationManager =  NotificationManagerCompat.from(this);
        setContentView(R.layout.activity_home);
        search = findViewById(R.id.search);
        menu = findViewById(R.id.button3);
        sp = findViewById(R.id.spinner);

        String [] items = new String[]{"Language","English","French","Hebrew"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items){
            @Override
            public boolean isEnabled(int position) {
                if (position==0){
                    return false;
                }
                else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position==0){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.isEnabled(0);
        sp.setAdapter(adapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomePageActivity.this,SearchActivity.class);
                startActivity(i);
            }
        });

        updateMayorMessage();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(HomePageActivity.this,view);
                popup.setOnMenuItemClickListener(HomePageActivity.this);
                popup.inflate(R.menu.navigation_menu);
                popup.show();
            }
        });

        addNewMessageListener();
    }

    private void updateMayorMessage() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("mayors");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren())
                {
                    Mayor mayor = snap.getValue(Mayor.class);
                    TextView tv = findViewById(R.id.mayorMessage);
                    tv.setText(tv.getText() + mayor.getLatest_post() + "\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static class Mayor{
        String city;
        String name;
        String latest_post;
        public Mayor()
        {

        }
        public Mayor(String city, String name, String last_post)
        {
            this.city= city;
            this.name = name;
            this.latest_post = last_post;
        }

        public String getCity() {
            return city;
        }

        public String getLatest_post() {
            return latest_post;
        }

        public String getName() {
            return name;
        }
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "chname";
            String description = "chdesc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void addNewMessageListener() {
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        DatabaseReference ref = data.getReference("mayors");
        //find my city <-

        String myCity = "Ariel";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL")
                .setSmallIcon(R.drawable.message_get)
                .setContentTitle("New mayor message")
                .setContentText(latest_post)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren())
                {
                    RegularRegistrationActivity.RegularProfile RP = snap.getValue(RegularRegistrationActivity.RegularProfile.class);
                    Mayor m = snap.getValue(Mayor.class);
                    latest_post = m.getLatest_post();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if the data changed, we need to check whether our city has new posts
                for(DataSnapshot snap: snapshot.getChildren())
                {
                    Mayor m = snap.getValue(Mayor.class);
                    if(!m.getLatest_post().equals(latest_post))
                    {
                        //push notification
                        notificationManager.notify(1, builder.build());
                        latest_post = m.getLatest_post();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void chooseChat(View v)
    {
        Intent intent = new Intent(HomePageActivity.this, ChooseChatActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.myprofile:
                Intent i;
                i = new Intent(HomePageActivity.this, EditRegularProfileActivity.class);
                startActivity(i);
                break;
            case R.id.settings:
                Intent j = new Intent(HomePageActivity.this,SettingsActivity.class);
                startActivity(j);
                break;
            case R.id.logOut:
                Intent k = new Intent(HomePageActivity.this,LoginActivity.class);
                startActivity(k);
                break;
        }
        return false;
    }
}