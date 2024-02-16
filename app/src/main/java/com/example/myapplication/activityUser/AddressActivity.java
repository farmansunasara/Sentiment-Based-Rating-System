package com.example.myapplication.activityUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.MyDatabaseHelper;
import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddressActivity extends AppCompatActivity {


    private TextInputEditText nameEditText;

    private TextInputEditText mobileEditText;
    private TextInputEditText addressEditText;
    private TextInputEditText cityEditText;
    private MaterialButton proceed;

    private TextInputEditText stateEditText;
    private TextInputEditText countryEditText;
    private TextInputEditText pincodeEditText;
    private MyDatabaseHelper databaseHelper;
    public String newemail;
    public String email;
    public String pwd;
    public String newPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);


        databaseHelper = new MyDatabaseHelper(this);

        // Initialize EditText fields
        nameEditText = findViewById(R.id.customernametxt);
        mobileEditText = findViewById(R.id.txtmobile);
        addressEditText = findViewById(R.id.txtAddress);
        cityEditText = findViewById(R.id.txtcity);

        stateEditText = findViewById(R.id.txtstate);
        pincodeEditText= findViewById(R.id.txtpincode);
        countryEditText =findViewById(R.id.txtcountry);
        proceed=findViewById(R.id.proceed);

        SharedPreferences loginPreference =this.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        String loggedInUserEmail = loginPreference.getString("userEmail", "");

        retrieveAndPopulateUserDetails(loggedInUserEmail);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();

                Intent intent = new Intent(AddressActivity.this, PaymentselectionActivity.class);
                startActivity(intent);
            }
        });

    }

    private void updateUserData() {
        // Retrieve new data from EditText fields
        String newName = nameEditText.getText().toString();
        String newEmail = email;
        String newMobile = mobileEditText.getText().toString();
        String newAddress = addressEditText.getText().toString();
        String newCity = cityEditText.getText().toString();
        String newState = stateEditText.getText().toString();
        String newPincode = pincodeEditText.getText().toString();
        String newCountry = countryEditText.getText().toString();
         newPassword = pwd;
        saveAddressToSharedPreferences(newAddress);


        // Update the database with the new data
        boolean isUpdated = databaseHelper.updateCustomerDetails(newName, newEmail, newMobile, newAddress, newCity, newState, newPincode, newCountry, newPassword);

        if (isUpdated) {
            // Data updated successfully


            Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();

            // You may show a toast or perform any other action here
        } else {
            // Failed to update data
            // You may show a toast or perform any other action here
        }
    }

    private void saveAddressToSharedPreferences(String address) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("address", address);
        editor.apply();
    }


    private void retrieveAndPopulateUserDetails(String loggedInUserEmail) {
        Cursor cursor = databaseHelper.viewCustomerDetailsforprofile(loggedInUserEmail);

        if (cursor != null) {
            // Check if the cursor has data
            if (cursor.moveToFirst()) {
                // Retrieve user details from the cursor
                int nameIndex = cursor.getColumnIndex(MyDatabaseHelper.COULMN_NAME);
                int emailIndex = cursor.getColumnIndex(MyDatabaseHelper.COULMN_EMAIL);
                int mobileIndex = cursor.getColumnIndex(MyDatabaseHelper.COULMN_MOBILE);
                int addressIndex = cursor.getColumnIndex(MyDatabaseHelper.COULMN_ADDRESS);
                int cityIndex = cursor.getColumnIndex(MyDatabaseHelper.COULMN_CITY);
                int stateIndex = cursor.getColumnIndex(MyDatabaseHelper.COULMN_STATE);
                int countryIndex = cursor.getColumnIndex(MyDatabaseHelper.COULMN_COUNTRY);
                int pincodeIndex = cursor.getColumnIndex(MyDatabaseHelper.COULMN_PINCODE);
                int pwdIndex = cursor.getColumnIndex(MyDatabaseHelper.COULMN_PASSWORD);



                if (nameIndex >= 0) {
                    String name = cursor.getString(nameIndex);
                    nameEditText.setText(name);
                }
                if (emailIndex >= 0) {
                     email = cursor.getString(emailIndex);
                    //emailEditText.setText(email);
                }
                if (mobileIndex >= 0) {
                    String mobile = cursor.getString(mobileIndex);
                    mobileEditText.setText(mobile);
                }
                if (addressIndex >= 0) {
                    String address = cursor.getString(addressIndex);
                    addressEditText.setText(address);
                }
                if (cityIndex >= 0) {
                    String city = cursor.getString(cityIndex);
                    cityEditText.setText(city);
                }
                if (stateIndex >= 0) {
                    String state = cursor.getString(stateIndex);
                    stateEditText.setText(state);
                }
                if (pincodeIndex >= 0) {
                    String pincode = cursor.getString(pincodeIndex);
                    pincodeEditText.setText(pincode);
                }
                if (countryIndex >= 0) {
                    String country = cursor.getString(countryIndex);
                    countryEditText.setText(country);
                }
                if (pwdIndex >= 0) {
                     pwd = cursor.getString(pwdIndex);
                   // pwdEditText.setText(pwd);
                }

            } else {
                // Handle case when cursor doesn't contain any data
                // You may display a message or perform another action here
            }

            // Close the cursor
            cursor.close();
        }

    }

}