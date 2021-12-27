package com.app.nextdoor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RegularRegistrationActivity extends AppCompatActivity {

    public class namesList {
        public ArrayList<String> citiesList;
        public namesList()
        {
            citiesList=  new ArrayList<>();
        }

        public String[] retrieveAsArray() {
            BufferedReader reader;
            try {
                reader =     reader = new BufferedReader(
                        new InputStreamReader(getAssets().open("cities_en.csv")));
                String line = reader.readLine();
                while (line != null) {
                    line = line.charAt(0) + line.substring(1).toLowerCase();
                    citiesList.add(line);
                    // read next line
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return citiesList.toArray(new String[0]);
        }
    }

    public static class RegularProfile implements Serializable {
        public String address;
        public String phone;
        public String age;
        public String fullName;
        public String job;
        public List<String> lang;
        public List<String> hobbies;

        public RegularProfile(){}

        public RegularProfile(String A, String P, String a, String F, String J,List<String> L, List<String> H){
            address=A;
            phone=P;
            age=a;
            fullName=F;
            job=J;
            lang=L;
            hobbies=H;
        }

        public String getAddress() {
            return address;
        }
        public String getPhoneNumber(){
            return phone;
        }

        public List<String> getHobbies() {
            return hobbies;
        }

        public List<String> getLang() {
            return lang;
        }

        public String getAge() {
            return age;
        }

        public String getFullName() {
            return fullName;
        }

        public String getJob() {
            return job;
        }


        @NonNull
        public String toString()
        {
            return "Name: " + fullName + "\nAddress:" + address;
        }

    }

    EditText Email, Password;
    EditText FullName;
    EditText PhoneNumber;
    EditText Age;
    EditText Job;
    EditText Languages;
    EditText Hobbies;
    Spinner Address;
    TextView Camera;
    TextView Gallery;
    ImageView Photo;
    ArrayList<String>arr_hobbies;
    ArrayAdapter<String> adapter;
    Button SignUp;
    TextView SignIn;
    FirebaseAuth myAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_registration);

        // Initialize
        Email = findViewById(R.id.Email2);
        Password = findViewById(R.id.Password2);
        FullName = findViewById(R.id.FullName);
        PhoneNumber = findViewById(R.id.PhoneNumber);
        Address = findViewById(R.id.spinner2);
        Age = findViewById(R.id.Activity);
        Job = findViewById(R.id.Servive);
        Camera = findViewById(R.id.cameraa);
        Gallery = findViewById(R.id.PRRE);
        Photo = findViewById(R.id.imageView18);
        Languages = findViewById(R.id.Descriptionnn);
        Hobbies = findViewById(R.id.hobiies);
        SignUp = findViewById(R.id.button2);
        SignIn = findViewById(R.id.textView2);
        myAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("/cities");


        namesList nl = new namesList();
        String [] items = nl.retrieveAsArray();
        databaseReference.child("cities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int index=1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String data = snapshot.getValue(String.class);
                    System.out.println(data);
                    items[index]=data;
                    index++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
        Address.setAdapter(adapter);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(RegularRegistrationActivity.this,LoginActivity.class);
                startActivity(j);
            }
        });

        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhotoFromGallery();
            }
        });
    }

    public void takePicture(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,0);
    }

    public void choosePhotoFromGallery(){
        Intent i = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                Photo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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
        else if(TextUtils.isEmpty(phoneNumber)){
            PhoneNumber.setError("Please enter your phone number");
            return false;
        }
        else if (!(TextUtils.isEmpty(email)) && !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            Email.setError("Please enter valid Email address");
            return false;
        }
        return true;
    }

    private void addToDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users/Regular");
        ArrayList<String> emptylist1 = new ArrayList<>();
        ArrayList<String> emptylist2 = new ArrayList<>();
        emptylist1.add("");
        emptylist2.add("");
        RegularProfile Rp = new RegularProfile(Address.getSelectedItem().toString(), PhoneNumber.getText().toString(),Age.getText().toString(), FullName.getText().toString(), Job.getText().toString(), emptylist1,emptylist2);
        reference.child(Objects.requireNonNull(myAuth.getUid())).setValue(Rp);
    }

    private void Register(){
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String fullName = FullName.getText().toString();
        String phoneNumber = PhoneNumber.getText().toString();
        String address = Address.getSelectedItem().toString();
        if (Validation(email,password,fullName,address,phoneNumber)){
            myAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        addToDatabase();
                            Intent i = new Intent(RegularRegistrationActivity.this, HomePageActivity.class);
                            startActivity(i);

                    }
                    else {
                        Toast.makeText(RegularRegistrationActivity.this,"Registration failed",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(RegularRegistrationActivity.this,"Registration failed",Toast.LENGTH_LONG).show();
        }
    }

}