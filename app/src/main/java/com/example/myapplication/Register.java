    package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

    public class Register extends AppCompatActivity {

        EditText customernametxt;
        EditText Emailtxt;
        EditText txtPwd;
        EditText txtmobile;

        EditText txtAddress;
        EditText txtcity;
        EditText txtstate;
        EditText txtcountry;
        EditText txtpincode;
        MaterialButton btnregister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        customernametxt=findViewById(R.id.customernametxt);
        Emailtxt=findViewById(R.id.Emailtxt);
        txtPwd=findViewById(R.id.txtPwd);
        txtmobile=findViewById(R.id.txtmobile);
        txtAddress=findViewById(R.id.txtAddress);
        txtcity=findViewById(R.id.txtcity);
        txtstate=findViewById(R.id.txtstate);
        txtcountry=findViewById(R.id.txtcountry);
        txtpincode=findViewById(R.id.txtpincode);
        btnregister=(MaterialButton) findViewById(R.id.btnregister);


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB =new MyDatabaseHelper(Register.this);
                myDB.addCustomer(customernametxt.getText().toString().trim(),Emailtxt.getText().toString().trim(),txtPwd.getText().toString().trim(),txtmobile.getText().toString().trim(), txtAddress.getText().toString().trim(),txtcity.getText().toString().trim(),txtstate.getText().toString().trim(),txtcountry.getText().toString().trim(),Integer.valueOf( txtpincode.getText().toString().trim()));
            }
        });

        TextView login = (TextView) findViewById(R.id.lnkLogin);
        login.setMovementMethod(LinkMovementMethod.getInstance());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }

}