package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


public class ProfileFragment extends Fragment {


        private TextInputEditText nameEditText;
        private TextInputEditText emailEditText;
        private TextInputEditText mobileEditText;
        private TextInputEditText addressEditText;
        private TextInputEditText cityEditText;
        private MaterialButton update;

    private TextInputEditText stateEditText;
    private TextInputEditText countryEditText;
    private TextInputEditText pincodeEditText;
    private TextInputEditText pwdEditText;


    // Add more fields as needed

        private MyDatabaseHelper databaseHelper;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile, container, false);
            return view;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            // Initialize database helper
            databaseHelper = new MyDatabaseHelper(getActivity());

            // Initialize EditText fields
            nameEditText = view.findViewById(R.id.customernametxt);
            emailEditText = view.findViewById(R.id.Emailtxt);
            mobileEditText = view.findViewById(R.id.txtmobile);
            addressEditText = view.findViewById(R.id.txtAddress);
            cityEditText = view.findViewById(R.id.txtcity);

            stateEditText = view.findViewById(R.id.txtstate);
            pincodeEditText= view.findViewById(R.id.txtpincode);
            countryEditText = view.findViewById(R.id.txtcountry);
            pwdEditText = view.findViewById(R.id.txtPwd);
            update=view.findViewById(R.id.update);


            // Initialize more EditText fields as needed

            // Retrieve user details and populate fields
            retrieveAndPopulateUserDetails();

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUserData();
                }
            });
        }

    private void updateUserData() {
        // Retrieve new data from EditText fields
        String newName = nameEditText.getText().toString();
        String newEmail = emailEditText.getText().toString();
        String newMobile = mobileEditText.getText().toString();
        String newAddress = addressEditText.getText().toString();
        String newCity = cityEditText.getText().toString();
        String newState = stateEditText.getText().toString();
        String newPincode = pincodeEditText.getText().toString();
        String newCountry = countryEditText.getText().toString();
        String newPassword = pwdEditText.getText().toString();

        // Update the database with the new data
        boolean isUpdated = databaseHelper.updateCustomerDetails(newName, newEmail, newMobile, newAddress, newCity, newState, newPincode, newCountry, newPassword);

        if (isUpdated) {
            // Data updated successfully

            Toast.makeText(getActivity(), "Data updated successfully", Toast.LENGTH_SHORT).show();

            // You may show a toast or perform any other action here
        } else {
            // Failed to update data
            // You may show a toast or perform any other action here
        }
    }

    private void retrieveAndPopulateUserDetails() {
        Cursor cursor = databaseHelper.viewCustomerDetails();

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
                    String email = cursor.getString(emailIndex);
                    emailEditText.setText(email);
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
                    String pwd = cursor.getString(pwdIndex);
                    pwdEditText.setText(pwd);
                }
            } else {
                // Handle case when cursor doesn't contain any data
                // You may display a message or perform another action here
            }

            // Close the cursor
            cursor.close();
        }

    }
    public void onDestroy() {
            super.onDestroy();
            // Close the database connection
            if (databaseHelper != null) {
                databaseHelper.close();
            }
        }
    }
