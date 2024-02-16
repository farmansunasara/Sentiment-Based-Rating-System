package com.example.myapplication.activityUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myapplication.R;

public class PaymentselectionActivity extends AppCompatActivity {



    private RadioGroup radioGroupPayment;
    private Button buttonProceedToCheckout;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentselection);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        buttonProceedToCheckout = findViewById(R.id.buttonProceedToCheckout);buttonProceedToCheckout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Get the selected payment method
                int selectedId = radioGroupPayment.getCheckedRadioButtonId();

                if (selectedId == -1) {
                    // No payment method selected
                    Toast.makeText(PaymentselectionActivity.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                } else {
                    // Payment method selected
                    RadioButton radioButton = findViewById(selectedId);
                    String selectedPaymentMethod = radioButton.getText().toString();

                    // Perform actions based on the selected payment method
                    if (selectedPaymentMethod.equals("Credit Card")) {
                        // Proceed to credit card payment activity
                        startActivity(new Intent(PaymentselectionActivity.this, CreditCardPaymentActivity.class));
                    } else if (selectedPaymentMethod.equals("PayPal")) {
                        // Proceed to PayPal payment activity
                        startActivity(new Intent(PaymentselectionActivity.this, PayPalPaymentActivity.class));
                    } else {
                        // Handle other payment methods
                        // Example: startActivity(new Intent(PaymentselectionActivity.this, OtherPaymentActivity.class));
                    }
                }
            }
        });
    }
    private void savePaymentMethod(String paymentMethod) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("paymentMethod", paymentMethod);
        editor.apply();
    }



}