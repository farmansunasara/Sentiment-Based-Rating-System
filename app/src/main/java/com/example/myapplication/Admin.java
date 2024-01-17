package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Admin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);



        ImageView leftIcon = findViewById(R.id.left_icon);
        TextView title =(TextView) findViewById(R.id.toolbar_title);

        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Admin.this, "Back",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Admin.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}