package com.outh.backend.controller;

import com.outh.backend.models.User;
import com.outh.backend.services.UserService;
import com.outh.backend.services.EmailService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${frontend.success.url}")
    private String successUrl;

    @Value("${frontend.cancel.url}")
    private String cancelUrl;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    // Create Checkout Session
    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, Object>> createCheckoutSession(@RequestBody Map<String, Object> request) {
        Object amountObj = request.get("amount");
        String userId = (String) request.get("userId");

        if (amountObj == null || userId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User ID and amount are required"));
        }

        long amount = ((Number) amountObj).longValue();

        try {
            Stripe.apiKey = stripeSecretKey;

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(cancelUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("zar")
                                                    .setUnitAmount(amount) // in cents
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Account Deposit")
                                                                    .build())
                                                    .build())
                                    .build())
                    .putMetadata("userId", userId)
                    .putMetadata("amount", String.valueOf(amount))
                    .build();

            Session session = Session.create(params);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("url", session.getUrl());

            return ResponseEntity.ok(responseData);

        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ 2. Webhook to handle success event and update balance
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            // Handle successful checkout session
            if ("checkout.session.completed".equals(event.getType())) {
                EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
                Session session = (Session) dataObjectDeserializer.getObject().orElse(null);

                if (session != null && session.getMetadata() != null) {
                    String userId = session.getMetadata().get("userId");
                    String amountStr = session.getMetadata().get("amount");

                    if (userId != null && amountStr != null) {
//                        BigDecimal amount = new BigDecimal(amountStr).divide(BigDecimal.valueOf(100)); // convert cents to Rands
//                        userService.updateUserBalance(userId, amount);
                        BigDecimal amount = new BigDecimal(amountStr).divide(BigDecimal.valueOf(100));
                        userService.addDeposit(userId, amount);

                    }
                }
            }

            return ResponseEntity.ok("Success");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Webhook Error: " + e.getMessage());
        }
    }

    @PostMapping("/verify-session")
    public ResponseEntity<Map<String, Object>> verifySession(@RequestBody Map<String, Object> body) {
        String sessionId = (String) body.get("sessionId");
        String userId = (String) body.get("userId");

        try {
            Session session = Session.retrieve(sessionId);

            if ("complete".equals(session.getStatus()) || "paid".equals(session.getPaymentStatus())) {
                long amount = session.getAmountTotal(); // cents
                BigDecimal amountDecimal = BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100));
                User updatedUser = userService.updateUserBalance(userId, amountDecimal);

                return ResponseEntity.ok(Map.of(
                        "message", "Balance updated successfully",
                        "updatedUser", updatedUser
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Payment not completed"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }





    @PostMapping("/withdraw-voucher")
    public ResponseEntity<Map<String, Object>> withdrawVoucher(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        Object amountObj = request.get("amount");
        String email = (String) request.get("email"); // read email from request

        if (userId == null || amountObj == null || email == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User ID, amount, and email are required"));
        }

        BigDecimal amount = new BigDecimal(amountObj.toString());
        String voucherCode = "VCHR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        try {
            // Withdraw and record transaction
            userService.withdrawVoucher(userId, amount, voucherCode);

            // Fetch updated user after withdrawal
            User updatedUser = userService.findById(userId);

            // Send voucher via provided email
            emailService.sendVoucherEmail(email, voucherCode, amount.toString());

            return ResponseEntity.ok(Map.of(
                    "message", "Voucher withdrawal successful",
                    "voucherCode", voucherCode,
                    "amount", amount,
                    "emailSentTo", email,
                    "updatedUser", updatedUser
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}




