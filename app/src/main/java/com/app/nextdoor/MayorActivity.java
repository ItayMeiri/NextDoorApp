package com.app.nextdoor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.textfield.TextInputLayout;

public class MayorActivity extends AppCompatActivity {

    String city = "Ashkelon"; // for testing

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mayor_layout);
    }

    public void onClick(View v)
    {
        TextInputLayout input = findViewById(R.id.mayorInput);
        String msg = input.getEditText().getText().toString();


        //send notification when button is clicked to whoever is in the same city as mayor
    }
}
