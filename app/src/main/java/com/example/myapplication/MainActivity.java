package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;

import android.widget.TextView;

import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.View;

import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    MyDatabaseHelper myDB;
    String username,password;
    SharedPreferences loginPreference;
    SharedPreferences.Editor loginPrefsEditor;
    Boolean savelogin;
    TextView  emailEditText;
    TextView passwordEditText;
    MaterialButton loginbtn;
    CheckBox rememberMeCheckbox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView register = (TextView) findViewById(R.id.lnkregister);
        register.setMovementMethod(LinkMovementMethod.getInstance());
        emailEditText = findViewById(R.id.emaillogin) ;
        passwordEditText = findViewById(R.id.passwordlogin);
         loginbtn= findViewById(R.id.loginbtn);
         rememberMeCheckbox = findViewById(R.id.remember_me_checkbox);
        TextView forgotPassword=findViewById(R.id.forgotPassword);
        loginPreference =getSharedPreferences("loginPrefs",MODE_PRIVATE);
        loginPrefsEditor = loginPreference.edit();

        myDB = new MyDatabaseHelper(MainActivity.this);

        savelogin= loginPreference.getBoolean("saveLogin",false);
        if (savelogin == true){
            emailEditText.setText(loginPreference.getString("username",""));
            passwordEditText.setText(loginPreference.getString("password",""));
            rememberMeCheckbox.setChecked(true);
        }

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

                            saveUserEmailToSharedPreferences(email);



                            Intent intent = new Intent(MainActivity.this, UserMain.class);
                            startActivity(intent);
                        } else if (emailEditText.getText().toString().equals("admin") && passwordEditText.getText().toString().equals("admin")) {
                            Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();

                            saveUserEmailToSharedPreferences("admin");

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

    public void onClick(View view) {
        if (view == loginbtn) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(emailEditText.getWindowToken(), 0);

            username =emailEditText.getText().toString();
            password = passwordEditText.getText().toString();

            if (rememberMeCheckbox.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", username);
                loginPrefsEditor.putString("password", password);
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }

            doSomethingElse();
        }
    }

    private void saveUserEmailToSharedPreferences(String email) {
        SharedPreferences loginPreference = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor loginPrefsEditor = loginPreference.edit();
        loginPrefsEditor.putString("userEmail", email);
        loginPrefsEditor.apply();
    }
    public void doSomethingElse() {
        startActivity(new Intent(MainActivity.this, UserMain.class));
        MainActivity.this.finish();
    }


}



