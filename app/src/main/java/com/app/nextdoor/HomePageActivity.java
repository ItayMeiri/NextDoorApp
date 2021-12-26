package com.app.nextdoor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.nextdoor.RegistrationActivity;


public class HomePageActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    Button search;
    Button menu;
    Spinner sp;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(HomePageActivity.this,view);
                popup.setOnMenuItemClickListener(HomePageActivity.this);
                popup.inflate(R.menu.navigation_menu);
                popup.show();
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
                Intent i = new Intent(HomePageActivity.this, MyRegularProfileActivity.class);
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