package com.app.nextdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    EditText Email, Password;
    EditText FullName;
    EditText PhoneNumber;
    EditText Address;
    Button SighUp;
    TextView SighIn;
    FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize
        Email = findViewById(R.id.Email2);
        Password = findViewById(R.id.Password2);
        FullName = findViewById(R.id.FullName);
        PhoneNumber = findViewById(R.id.PhoneNumber);
        Address = findViewById(R.id.Address);
        SighUp = findViewById(R.id.button2);
        SighIn = findViewById(R.id.textView2);
        myAuth = FirebaseAuth.getInstance();

        SighUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        SighIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void Validation(String email,String password){
        if (TextUtils.isEmpty(email)){
            Email.setError("Please enter your email address");
        }
        else if (TextUtils.isEmpty(password)){
            Password.setError("Please enter your password");
        }
        else if (!(TextUtils.isEmpty(email)) && !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            Email.setError("Please enter valid Email address");
        }
    }

    private void Register(){
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        Validation(email,password);
        myAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent i = new Intent(RegistrationActivity.this,HomePageActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(RegistrationActivity.this,"Registration field",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}