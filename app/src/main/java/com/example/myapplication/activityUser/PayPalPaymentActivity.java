package com.example.myapplication.activityUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

public class PayPalPaymentActivity extends AppCompatActivity {
    private TextInputEditText editTextPayPalEmail;
    private Button buttonPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_payment);
        // Initialize views
        editTextPayPalEmail = findViewById(R.id.editTextPayPalEmail);
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
        String email = editTextPayPalEmail.getText().toString().trim();

        // Perform validation here
        // For example, check if the email is valid

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid PayPal email", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Process payment
    private void processPayment() {
        // Implement payment processing logic here
        // For example, you can integrate with PayPal SDK to process real payments
        Toast.makeText(this, "Payment processed successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PayPalPaymentActivity.this, OrderSummaryActivity.class);
        startActivity(intent);
        // You can implement further actions here, such as navigating to the order confirmation page
    }
}