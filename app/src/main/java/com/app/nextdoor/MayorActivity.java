package com.app.nextdoor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MayorActivity extends AppCompatActivity {

    String city = "Ashkelon"; // for testing
    HomePageActivity.Mayor m;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mayor_layout);
    }

    public void onClick(View v)
    {
        TextInputLayout input = findViewById(R.id.textInputLayout);
        String msg = input.getEditText().getText().toString();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("mayors/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                m = snapshot.getValue(HomePageActivity.Mayor.class);
                m.latest_post = msg;
                input.getEditText().setText("");
                update();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //send notification when button is clicked to whoever is in the same city as mayor
    }
    private void update() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("mayors/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.setValue(m);
    }
}
