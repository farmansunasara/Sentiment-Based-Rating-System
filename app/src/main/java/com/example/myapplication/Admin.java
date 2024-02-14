package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class Admin extends AppCompatActivity {


    public static final String SHARED_PREFS = "sharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        MaterialButton logout=findViewById(R.id.logout);

        TextView title =(TextView) findViewById(R.id.toolbar_title);
        MaterialCardView category = findViewById(R.id.category);
        MaterialCardView product = findViewById(R.id.product);
        MaterialCardView vieworder = findViewById(R.id.vieworder);
        MaterialCardView sliderimage = findViewById(R.id.sliderimage);
        MaterialCardView managecustomer = findViewById(R.id.managecustomer);




        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, com.example.myapplication.category.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name","");
                editor.apply();

                Toast.makeText(Admin.this, "Back",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Admin.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(intent);
            }
        });


        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, com.example.myapplication.category.class);
                startActivity(intent);
            }
        });

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, com.example.myapplication.view_product.class);
                startActivity(intent);
            }
        });

        vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, com.example.myapplication.view_orders.class);
                startActivity(intent);
            }
        });

        sliderimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, com.example.myapplication.slider_image.class);
                startActivity(intent);
            }
        });

        managecustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, com.example.myapplication.Manage_customer.class);
                startActivity(intent);
            }
        });
    }
}