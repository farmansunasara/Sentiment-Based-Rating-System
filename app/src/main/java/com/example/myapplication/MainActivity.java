package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
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

//    CheckBox remember_me_checkbox = findViewById(R.id.remember_me_checkbox);

    public static final String SHARED_PREFS = "sharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView register = (TextView) findViewById(R.id.lnkregister);
        register.setMovementMethod(LinkMovementMethod.getInstance());
        TextView username =(TextView) findViewById(R.id.emaillogin) ;
        TextView password=(TextView) findViewById(R.id.passwordlogin);
        MaterialButton loginbtn =(MaterialButton) findViewById(R.id.loginbtn);
           checkBox();




        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    Toast.makeText(MainActivity.this,"LOGIN Successfull",Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name","true");
                    editor.apply();


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

    private void checkBox() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String check = sharedPreferences.getString("name","");
        if(check.equals("true")){
            Toast.makeText(MainActivity.this,"LOGIN Successfull",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Admin.class);
            startActivity(intent);
            finish();

        }
    }
}



