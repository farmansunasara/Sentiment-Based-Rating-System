    package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

    public class Register extends AppCompatActivity {

        EditText customernametxt;
        EditText Emailtxt;
        EditText txtPwd;
        EditText txtrePwd;
        EditText txtmobile;

        EditText txtAddress;
        EditText txtcity;
        EditText txtstate;
        EditText txtcountry;
        EditText txtpincode;
        MaterialButton btnregister;
        // one boolean variable to check whether all the text fields
        // are filled by the user, properly or not.
        boolean isAllFieldsChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        customernametxt=findViewById(R.id.customernametxt);
        Emailtxt=findViewById(R.id.Emailtxt);
        txtPwd=findViewById(R.id.txtPwd);
        txtrePwd=findViewById(R.id.txtrePwd);
        txtmobile=findViewById(R.id.txtmobile);
        txtAddress=findViewById(R.id.txtAddress);
        txtcity=findViewById(R.id.txtcity);
        txtstate=findViewById(R.id.txtstate);
        txtcountry=findViewById(R.id.txtcountry);
        txtpincode=findViewById(R.id.txtpincode);
        btnregister= findViewById(R.id.btnregister);


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB =new MyDatabaseHelper(Register.this);
                isAllFieldsChecked = CheckAllFields();
                if(isAllFieldsChecked) {
                    myDB.addCustomer(customernametxt.getText().toString().trim(), Emailtxt.getText().toString().trim(), txtPwd.getText().toString().trim(), txtmobile.getText().toString().trim(), txtAddress.getText().toString().trim(), txtcity.getText().toString().trim(), txtstate.getText().toString().trim(), txtcountry.getText().toString().trim(), Integer.valueOf(txtpincode.getText().toString().trim()));
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    startActivity(intent);
                }
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

        private boolean CheckAllFields() {
            if (customernametxt.length() == 0) {
                customernametxt.setError("This field is required");
                return false;
            }

            if (Emailtxt.length() == 0) {
                Emailtxt.setError("This field is required");
                return false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(Emailtxt.getText().toString().trim()).matches()) {
                Emailtxt.setError("Invalid Email!");
            }



            if (txtmobile.length() == 0) {
                txtmobile.setError("This field is required");
                return false;
            }else if (txtmobile.length() != 10) {
                txtmobile.setError("Pin no must be a number and should be 10 digits");
                return false;
            }
            else if (!Patterns.PHONE.matcher(txtmobile.getText().toString().trim()).matches()) {
                txtmobile.setError("Invalid Mobile no");
                return false;
            }

            if (txtAddress.length() == 0) {
                txtAddress.setError("This field is required");
                return false;
            }
            if (txtcity.length() == 0) {
                txtcity.setError("This field is required");
                return false;
            }
            if (txtstate.length() == 0) {
                txtstate.setError("This field is required");
                return false;
            }

            if (txtpincode.length() == 0) {
                txtpincode.setError("This field is required");
                return false;
            }else if (txtpincode.length() != 6) {
                txtpincode.setError("Pincode no must be a number and should be 6 digits");
                return false;
            }
            else if (!Patterns.PHONE.matcher(txtmobile.getText().toString().trim()).matches()) {
                txtpincode.setError("Invalid Pincode no");
                return false;
            }

            if (txtcountry.length() == 0) {
                txtcountry.setError("This field is required");
                return false;
            }

            if (txtPwd.length() == 0) {
                txtPwd.setError("This field is required");
                return false;
            } else if (txtPwd.length() < 8) {
                txtPwd.setError("Password must be minimum 8 characters");
                return false;
            }

            if (txtrePwd.length() == 0) {
                txtrePwd.setError("This field is required");
                return false;
            } else if (txtrePwd.length() < 8) {
                txtrePwd.setError("Password must be minimum 8 characters");
                return false;
            } else if (!txtPwd.getText().toString().equals(txtrePwd.getText().toString())) {
                txtrePwd.setError("Passwords should be the same");
                return false;
            }
            // after all validation return true.
            return true;
        }

}