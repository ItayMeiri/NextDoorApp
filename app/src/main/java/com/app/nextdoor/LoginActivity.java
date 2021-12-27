package com.app.nextdoor;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    EditText Email, Password;
    Button SignIn;
    TextView SignUp;
    TextView ResetPassword;
    FirebaseAuth myAuth;
    CheckBox RememberMe;
    private SharedPreferences Sp;
    private SharedPreferences.Editor remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.Email1);
        Password = findViewById(R.id.Password1);
        SignIn = findViewById(R.id.button1);
        SignUp = findViewById(R.id.textView1);
        ResetPassword = findViewById(R.id.forgetpassword);
        myAuth = FirebaseAuth.getInstance();

        RememberMe = findViewById(R.id.checkBox2);
        Sp = getSharedPreferences("rememberLogin", MODE_PRIVATE);
        remember = Sp.edit();
        boolean rememberMe = Sp.getBoolean("rememberMe", false);
        if (rememberMe) {
            Email.setText(Sp.getString("email", ""));
            Password.setText(Sp.getString("password", ""));
            RememberMe.setChecked(true);
        }


        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
                rememberMe();
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });

        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword(myAuth,Email.getText().toString());
            }
        });
    }

    private boolean Validation(String email, String password){
        if (TextUtils.isEmpty(email)){
            Email.setError("Please enter your Email address");
            return false;
        }
        else if (TextUtils.isEmpty(password)){
            Password.setError("Please enter your password");
            return false;
        }
        else if (!(TextUtils.isEmpty(email)) && !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            Email.setError("Please enter valid Email address");
            return false;
        }
        return true;
    }

    private void Login(){
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        if (Validation(email,password)){
            myAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent i = new Intent(LoginActivity.this,HomePageActivity.class);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_LONG).show();
        }
    }

    public void resetPassword(FirebaseAuth auth,String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,"A password reset message has been sent to an email that exists in the system",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(LoginActivity.this,"Action failed. Please try again",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void rememberMe (){
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        if (RememberMe.isChecked()) {
            remember.putBoolean("rememberMe", true);
            remember.putString("email", email);
            remember.putString("password", password);
        } else {
            remember.clear();
        }
        remember.commit();
    }

}
