package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

import android.widget.TextView;

import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.View;

import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

//    CheckBox remember_me_checkbox = findViewById(R.id.remember_me_checkbox);
    MyDatabaseHelper myDB;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "login_pref";
    private static final String PREF_KEY_REMEMBER_ME = "remember_me";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView register = (TextView) findViewById(R.id.lnkregister);
        register.setMovementMethod(LinkMovementMethod.getInstance());
        TextView  emailEditText =(TextView) findViewById(R.id.emaillogin) ;
        TextView passwordEditText =(TextView) findViewById(R.id.passwordlogin);
        MaterialButton loginbtn =(MaterialButton) findViewById(R.id.loginbtn);
        CheckBox rememberMeCheckbox = findViewById(R.id.remember_me_checkbox);
        TextView forgotPassword=findViewById(R.id.forgotPassword);

        myDB = new MyDatabaseHelper(MainActivity.this);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);




        // Load the state of remember me checkbox from SharedPreferences
        boolean rememberMeChecked = sharedPreferences.getBoolean(PREF_KEY_REMEMBER_ME, false);
        rememberMeCheckbox.setChecked(rememberMeChecked);

        // Set up OnCheckedChangeListener for remember me checkbox
        rememberMeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the state of remember me checkbox to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREF_KEY_REMEMBER_ME, isChecked);
            editor.apply();
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(email.equals("")||password.equals("")) {
                    Toast.makeText(MainActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }
                else{
                        boolean emailPasswordMatched = myDB.checkEmailPassword(email, password);
                        if (emailPasswordMatched) {
                            Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            // Proceed to next activity or perform other actions


                            Intent intent = new Intent(MainActivity.this, UserMain.class);
                            startActivity(intent);
                        } else if (emailEditText.getText().toString().equals("admin") && passwordEditText.getText().toString().equals("admin")) {
                            Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();



                            Intent intent = new Intent(MainActivity.this, Admin.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        });
// Handle the click event of "Forgot Password" textview
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the activity where the user can reset their password
                Intent intent = new Intent(MainActivity.this, ResetPaswordActivity.class);
                startActivity(intent);
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



