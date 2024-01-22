package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

public class Admin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);



        ImageView leftIcon = findViewById(R.id.left_icon);
        TextView title =(TextView) findViewById(R.id.toolbar_title);
        MaterialCardView category = findViewById(R.id.category);
        MaterialCardView product = findViewById(R.id.product);
        MaterialCardView vieworder = findViewById(R.id.vieworder);
        MaterialCardView sliderimage = findViewById(R.id.sliderimage);
        MaterialCardView managecustomer = findViewById(R.id.managecustomer);



        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Admin.this, "Back",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Admin.this, MainActivity.class);
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
                Intent intent = new Intent(Admin.this, com.example.myapplication.Add_product_form.class);
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