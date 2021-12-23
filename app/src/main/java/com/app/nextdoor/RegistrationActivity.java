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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.io.Serializable;
import java.util.Scanner;


public class RegistrationActivity extends AppCompatActivity {


    public static class RegularProfile implements Serializable{
        public String Address;
        public String PhoneNumber;
        public int Age;
        public String FullName;
        public String Job;
        public List<String> Lang;
        public List<String> Hobbies;

        public RegularProfile(){}

        public RegularProfile(String A, String P, int a, String F, String J,List<String> L, List<String> H){
            Address=A;
            PhoneNumber=P;
            Age=a;
            FullName=F;
            Job=J;
            Lang=L;
            Hobbies=H;
        }

        @NonNull
        public String toString()
        {
            return "Name: " + FullName + "\nAddress:" + Address;
        }

    }

    public static class BusinessProfile {

        public String Address;
        public String PhoneNumber;
        public String FullName;
        public List<String> ServiceLang;
        public String ActivityTime;
        public String Description;

        public BusinessProfile(){}

        public BusinessProfile(String A, String P, String F, List<String> sL,String aT, String D){
            Address=A;
            PhoneNumber=P;
            FullName=F;
            ServiceLang=sL;
            ActivityTime=aT;
            Description=D;
        }
    }

    EditText Email, Password;
    EditText FullName;
    EditText PhoneNumber;
    EditText Address;
    Button SignUp;
    TextView SignIn;
    RadioGroup radioGroup;
    RadioButton Regular;
    RadioButton Business;
    FirebaseAuth myAuth;
    String userType;

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
        SignUp = findViewById(R.id.button2);
        SignIn = findViewById(R.id.textView2);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        Regular = (RadioButton) findViewById(R.id.Regular);
        Business = (RadioButton) findViewById(R.id.Business);
        userType = "";
        myAuth = FirebaseAuth.getInstance();


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Regular.isChecked()){
                    userType="Regular";
                }
                if (Business.isChecked()){
                    userType="Business";
                }
                Register();
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean Validation(String email,String password, String fullName, String address, String phoneNumber){
        if (TextUtils.isEmpty(fullName)){
            FullName.setError("Please enter your full Name");
            return false;
        }
        else if (TextUtils.isEmpty(email)){
            Email.setError("Please enter your email address");
            return false;
        }
        else if (TextUtils.isEmpty(password)){
            Password.setError("Please enter your password");
            return false;
        }
        else if (TextUtils.isEmpty(address)){
            Address.setError("Please enter your address");
            return false;
        }
        else if(TextUtils.isEmpty(phoneNumber)){
            PhoneNumber.setError("Please enter your phone number");
            return false;
        }
        else if (!(TextUtils.isEmpty(email)) && !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            Email.setError("Please enter valid Email address");
            return false;
        }
        else if (!(Regular.isChecked())&&!(Business.isChecked())) {
            Toast.makeText(RegistrationActivity.this,"Please choose profile type",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private void addToDatabase(String userType){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users/"+ userType);
        if (userType.equals("Regular")){
            RegularProfile Rp = new RegularProfile(Address.getText().toString(), PhoneNumber.getText().toString(),20, FullName.getText().toString(), "", new ArrayList<String>(),new ArrayList<String>());
            reference.child(Objects.requireNonNull(myAuth.getUid())).setValue(Rp);
        }
        if (userType.equals("Business")){
            BusinessProfile Bp = new BusinessProfile(Address.getText().toString(), PhoneNumber.getText().toString(), FullName.getText().toString(), new ArrayList<String>(), "00:00 - 00:00","");
            reference.child(Objects.requireNonNull(myAuth.getUid())).setValue(Bp);
        }
    }

    private void Register(){
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String fullName = FullName.getText().toString();
        String phoneNumber = PhoneNumber.getText().toString();
        String address = Address.getText().toString();
        if (Validation(email,password,fullName,address,phoneNumber)){
            myAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        addToDatabase(userType);
                        if (userType=="Regular"){
                            Intent i = new Intent(RegistrationActivity.this,MyRegularProfileActivity.class);
                            startActivity(i);
                        }
                        else {
                            Intent i = new Intent(RegistrationActivity.this,MyBusinessProfileActivity.class);
                            startActivity(i);
                        }
                    }
                    else {
                        Toast.makeText(RegistrationActivity.this,"Registration failed",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(RegistrationActivity.this,"Registration failed",Toast.LENGTH_LONG).show();
        }
    }
}