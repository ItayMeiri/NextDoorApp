package com.app.nextdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity {

    TextView Regular;
    TextView Business;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Regular = findViewById(R.id.textView);
        Business = findViewById(R.id.textView3);

        Regular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrationActivity.this,RegularRegistrationActivity.class);
                startActivity(i);
            }
        });

        Business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrationActivity.this,BusinessRegistrationActivity.class);
                startActivity(i);
            }
        });

    }
}