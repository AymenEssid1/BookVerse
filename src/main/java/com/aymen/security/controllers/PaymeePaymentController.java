package com.aymen.security.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/payment")
public class PaymeePaymentController {

    private final String API_KEY = "c7fd2d8395560258ebbc82ad430a8a6eec648438";
    private final String BASE_URL = "https://sandbox.paymee.tn/api/v1/payments";

    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String createPayment() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token c7fd2d8395560258ebbc82ad430a8a6eec648438");

        // Construct the request payload
        String requestPayload = "{\"vendor\": 1265, \"amount\": 45, \"note\": \"Commande #1324\"}";

        HttpEntity<String> entity = new HttpEntity<>(requestPayload, headers);

        // Make the POST request
        ResponseEntity<String> response = restTemplate.exchange( "https://sandbox.paymee.tn/api/v1/payments/create", HttpMethod.POST, entity, String.class);


        return response.getBody();
    }


}

