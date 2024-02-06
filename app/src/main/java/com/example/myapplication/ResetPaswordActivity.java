package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPaswordActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private MyDatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pasword);

        editTextEmail = findViewById(R.id.editTextEmail);
        Button buttonResetPassword = findViewById(R.id.buttonResetPassword);

        myDB = new MyDatabaseHelper(this);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (myDB.checkEmailExists(email)) {
                    // Perform password reset action here, such as sending an email with a temporary password
                    // You can implement your logic here, like generating a temporary password and sending it to the user's email
                    // For this example, let's just show a message
                    Toast.makeText(getApplicationContext(), "Password reset email sent!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Email not registered!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}