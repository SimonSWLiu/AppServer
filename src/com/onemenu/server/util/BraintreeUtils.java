package com.onemenu.server.util;

import java.math.BigDecimal;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;

public class BraintreeUtils {

    private static final String propertiesFileName = "/BraintreeGateway.properties";

    private static String MERCHANT_ID = PropertiesUtils.getProperties("merchantId",
            propertiesFileName);
    private static String PUBLIC_KEY = PropertiesUtils.getProperties("publicKey",
            propertiesFileName);
    private static String PRIVATE_KEY = PropertiesUtils.getProperties("privateKey",
            propertiesFileName);


    // private static String MERCHANT_ID = "k2p6c3t6hmynrcdx";
    // private static String PUBLIC_KEY = "ygp4tb4wppsz6qfd";
    // private static String PRIVATE_KEY = "eb4055661a832055a9720a59f767a466";

    // private static BraintreeGateway gateway = new BraintreeGateway(Environment.SANDBOX,
    // "kryzysk7cjyzxt2p", "zp6vh37sy8y5fht2", "bad26846d55f2dbc7ebb49a1a413f629");

    private static BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION,
            MERCHANT_ID, PUBLIC_KEY, PRIVATE_KEY);

    public static String getClientToken() {

        ClientTokenRequest clientTokenRequest = new ClientTokenRequest();
        String clientToken = gateway.clientToken().generate(clientTokenRequest);
        return clientToken;
    }


    public static Result<Transaction> purchases(String nonce, String amount) {

        // TransactionRequest transactionRequest = new TransactionRequest()
        // .amount(new BigDecimal(amount))
        // .orderId("1234567890")
        // .merchantAccountId("kryzysk7cjyzxt2p")
        // .paymentMethodNonce(nonce)
        // .customer()
        // .firstName("Drew")
        // .lastName("Smith")
        // .company("Braintree")
        // .phone("312-555-1234")
        // .fax("312-555-1235")
        // .website("http://www.example.com")
        // .email("drew@example.com")
        // .done()
        // .billingAddress()
        // .firstName("Paul")
        // .lastName("Smith")
        // .company("Braintree")
        // .streetAddress("1 E Main St")
        // .extendedAddress("Suite 403")
        // .locality("Chicago")
        // .region("IL")
        // .postalCode("60622")
        // .countryCodeAlpha2("US")
        // .done()
        // .shippingAddress()
        // .firstName("Jen")
        // .lastName("Smith")
        // .company("Braintree")
        // .streetAddress("1 E 1st St")
        // .extendedAddress("Suite 403")
        // .locality("Bartlett")
        // .region("IL")
        // .postalCode("60103")
        // .countryCodeAlpha2("US")
        // .done()
        // .options()
        // .submitForSettlement(true)
        // .done()
        // .channel("MyShoppingCartProvider");

        // TransactionRequest transactionRequest =
        // new TransactionRequest().amount(new BigDecimal(amount)).paymentMethodNonce(nonce)
        // .options().submitForSettlement(true).done();
        
        TransactionRequest transactionRequest =
                new TransactionRequest().amount(new BigDecimal(amount)).paymentMethodNonce(nonce);

        Result<Transaction> transactionResult = gateway.transaction().sale(transactionRequest);

        return transactionResult;
    }
}
