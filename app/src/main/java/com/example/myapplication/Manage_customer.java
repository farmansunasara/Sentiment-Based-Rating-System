package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class Manage_customer extends AppCompatActivity {
    MyDatabaseHelper myDB;
    RecyclerView recyclerView;
    CustomAdapter customAdapter;

    ImageView left_icon_customer;
    ArrayList<String> cust_id,cust_name,cust_Email,cust_pwd,cust_mobile,cust_address,cust_city,cust_state,cust_country,cust_pincode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customer);
        Log.d("Manage_customer", "onCreate");

        left_icon_customer = findViewById(R.id.left_icon_customer);
        left_icon_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Manage_customer.this, "Back", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Manage_customer.this, Admin.class);
                startActivity(intent);
                finish(); // Optional: finish the current activity to remove it from the stack
            }
        });
        recyclerView =findViewById(R.id.recyclerview);
        myDB = new MyDatabaseHelper(Manage_customer.this);
        cust_id=new ArrayList<>();
        cust_name=new ArrayList<>();
        cust_Email=new ArrayList<>();
        cust_pwd=new ArrayList<>();
        cust_mobile=new ArrayList<>();
        cust_address=new ArrayList<>();
        cust_city=new ArrayList<>();
        cust_state=new ArrayList<>();
        cust_country=new ArrayList<>();
        cust_pincode=new ArrayList<>();

        Log.d("Manage_customer", "left_icon_customer: " + left_icon_customer);


        storeDataInArrays();

        customAdapter = new CustomAdapter(Manage_customer.this,cust_id,cust_name,cust_Email,cust_pwd,cust_mobile,cust_address,cust_city
                ,cust_state,cust_country,cust_pincode);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Manage_customer.this));




    }

    void storeDataInArrays(){
        Cursor cursor = myDB.viewCustomerDetails();
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();

        }else {
            while (cursor.moveToNext()){
                cust_id.add(cursor.getString(0));
                cust_name.add(cursor.getString(1));
                cust_Email.add(cursor.getString(2));
                cust_pwd.add(cursor.getString(3));
                cust_mobile.add(cursor.getString(4));
                cust_address.add(cursor.getString(5));
                cust_city.add(cursor.getString(6));
                cust_state.add(cursor.getString(7));
                cust_country.add(cursor.getString(8));
                cust_pincode.add(cursor.getString(9));

            }
        }
    }

}