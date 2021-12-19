package com.app.nextdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    public static class RegularProfile {
        public String Address;
        public String PhoneNumber;
        public String FullName;
        public String Job;
        public List<String> Hobbies;

        public RegularProfile(){

        }

        public RegularProfile(String A, String P, String F, String J, List<String> H){
            Address=A;
            PhoneNumber=P;
            FullName=F;
            Job=J;
            Hobbies=H;
        }

    }

    public static class BusinessProfile {

        public String Address;
        public String PhoneNumber;
        public String FullName;
        public String ServiceLang;
        public String ActivityTime;
        public String Description;


        public BusinessProfile(){

        }

        public BusinessProfile(String A, String P, String F, String sL,String aT, String D){
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
        else if (!(Regular.isChecked())&&!(Business.isChecked())) {
            Regular.setError("Please choose account type");

        }
        }


    private void addToDatabase(String userType){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users/"+ userType);
        if (userType.equals("Regular")){
            RegularProfile Rp = new RegularProfile(Address.getText().toString(), PhoneNumber.getText().toString(), FullName.getText().toString(), "", new ArrayList<String>());
            reference.child(Objects.requireNonNull(myAuth.getUid())).setValue(Rp);
        }
        if (userType.equals("Business")){
            BusinessProfile Bp = new BusinessProfile(Address.getText().toString(), PhoneNumber.getText().toString(), FullName.getText().toString(), "", "00:00 - 00:00","");
            reference.child(Objects.requireNonNull(myAuth.getUid())).setValue(Bp);
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
                    addToDatabase(userType);
                    Intent i = new Intent(RegistrationActivity.this,HomePageActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(RegistrationActivity.this,"Registration failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}