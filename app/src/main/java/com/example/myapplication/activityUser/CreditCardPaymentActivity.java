package com.example.myapplication.activityUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;

public class CreditCardPaymentActivity extends AppCompatActivity {

    private EditText editTextCardNumber;
    private EditText editTextExpirationDate;
    private EditText editTextCVV;
    private Button buttonPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_payment); editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextExpirationDate = findViewById(R.id.editTextExpirationDate);
        editTextCVV = findViewById(R.id.editTextCVV);
        buttonPay = findViewById(R.id.buttonPay);

        // Set OnClickListener for Pay button
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate and process payment
                if (validatePayment()) {
                    // Payment is valid, proceed with payment processing
                    processPayment();
                }
            }
        });
    }

    // Validate payment details
    private boolean validatePayment() {
        String cardNumber = editTextCardNumber.getText().toString().trim();
        String expirationDate = editTextExpirationDate.getText().toString().trim();
        String cvv = editTextCVV.getText().toString().trim();

        // Perform validation here
        // For example, check if the fields are not empty

        if (cardNumber.isEmpty() || expirationDate.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Process payment
    private void processPayment() {
        // Implement payment processing logic here
        Toast.makeText(this, "Payment processed successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreditCardPaymentActivity.this, OrderSummaryActivity.class);
        startActivity(intent);
        // You can implement further actions here, such as navigating to the order confirmation page
    }
}