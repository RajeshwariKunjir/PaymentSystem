package com.example.paymentsystem;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class PaymentGateway {
    private static final String STRIPE_SECRET_KEY = "sk_test_51NNqxWSJK1wP9hOO4c92ORtUNn2E0d1QwhRXpr129p4uHwyo6AbEwSMZ52Rfa3SoxzlWfvXeWYzpu4DMUQk7Wh3o00RudzNPEe";

    public static void main(String[] args) {
        // Set your secret key
        Stripe.apiKey = STRIPE_SECRET_KEY;

        // Create a payment intent
        PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                .setAmount(10L)  // Amount in cents
                .setCurrency("rupee")
                .addPaymentMethodType("card")
                .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(createParams);
            System.out.println("Payment Intent ID: " + paymentIntent.getId());

            // Handle the payment response
            if (paymentIntent.getStatus().equals("requires_confirmation")) {
                System.out.println("Payment requires confirmation");
                // Add code for confirming the payment
            } else if (paymentIntent.getStatus().equals("succeeded")) {
                System.out.println("Payment succeeded");
                // Add code for handling successful payment
            }
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}
