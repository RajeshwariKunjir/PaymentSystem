package com.example.paymentsystem;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

public class MainActivity extends AppCompatActivity {

    private static final String STRIPE_PUBLISHABLE_KEY = "pk_test_51NNqxWSJK1wP9hOOETf0s8PtOoZwj9u0SQRzseaaseXE6o5rhSNrxvXKVdDwwvHtyrVl0o4oFTwF8Yh4LHvwKayj00amaTTlUz";
    private static final String PAYMENT_INTENT_CLIENT_SECRET = "sk_test_51NNqxWSJK1wP9hOO4c92ORtUNn2E0d1QwhRXpr129p4uHwyo6AbEwSMZ52Rfa3SoxzlWfvXeWYzpu4DMUQk7Wh3o00RudzNPEe";

    private Stripe stripe;

    private CardInputWidget cardInputWidget;
    private Button payButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set your publishable key
        stripe = new Stripe(getApplicationContext(), STRIPE_PUBLISHABLE_KEY);

        cardInputWidget = findViewById(R.id.card_input_widget);
        payButton = findViewById(R.id.pay_button);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });
    }

    private void pay() {
        PaymentMethodCreateParams paymentMethodParams = cardInputWidget.getPaymentMethodCreateParams();
        if (paymentMethodParams != null) {
            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(paymentMethodParams, PAYMENT_INTENT_CLIENT_SECRET);
            stripe.confirmPayment(MainActivity.this, confirmParams);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        stripe.onPaymentResult(requestCode, data, new ApiResultCallback<PaymentIntentResult>() {
            @Override
            public void onSuccess(PaymentIntentResult result) {
                PaymentIntent paymentIntent = result.getIntent();
                PaymentIntent.Status status = paymentIntent.getStatus();
                if (status == PaymentIntent.Status.Succeeded) {
                    // Payment succeeded

                    System.out.println("Payment succeeded");
                } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                    // Payment requires a new payment method
                    System.out.println("Payment requires confirmation");
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Payment failed. Error: " + e.getMessage());
            }

        });
    }
}
