package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView register = (TextView) findViewById(R.id.lnkregister);
        register.setMovementMethod(LinkMovementMethod.getInstance());
        TextView username =(TextView) findViewById(R.id.emaillogin) ;
        TextView password=(TextView) findViewById(R.id.passwordlogin);
        MaterialButton loginbtn =(MaterialButton) findViewById(R.id.loginbtn);




        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    Toast.makeText(MainActivity.this,"LOGIN Successfull",Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(MainActivity.this, Admin.class);
                        startActivity(intent);



                }else {
                    Toast.makeText(MainActivity.this,"LOGIN UnSuccessfull",Toast.LENGTH_SHORT).show();

                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });


    }
}