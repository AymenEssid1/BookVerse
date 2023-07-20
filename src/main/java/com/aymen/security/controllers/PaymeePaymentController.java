package com.aymen.security.controllers;

import com.aymen.security.purchase.cart.CartService;
import com.aymen.security.purchase.order.Order;
import com.aymen.security.purchase.order.OrderResponse;
import com.aymen.security.purchase.order.OrderService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymeePaymentController {

    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();
    @Autowired
    OrderService orderService;
    @Autowired
    CartService cartService;



    @RequestMapping(value = "check", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkPaymentStatus(@RequestParam Integer orderid, @RequestParam String token) {
        String url = "https://sandbox.paymee.tn/api/v1/payments/" + token + "/check";

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token 86bcdfec6a54e163bfe3f51921d36782d96697ae");

        String requestPayload = "{}";
        HttpEntity<String> entity = new HttpEntity<>(requestPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            System.out.println(responseBody);
            JSONObject json = new JSONObject(responseBody);

            boolean status = json.getBoolean("status");
            if (status) {
                boolean paymentStatus = json.getJSONObject("data").getBoolean("payment_status");
                if (paymentStatus) {
                    orderService.purchaseComplete(orderid);

                    Map<String, Object> successResponse = new HashMap<>();
                    successResponse.put("status", "success");
                    successResponse.put("message", "Payment is successful");

                    return ResponseEntity.status(HttpStatus.OK).body(successResponse);
                } else {
                    orderService.deleteOrder(orderid);

                    Map<String, Object> failureResponse = new HashMap<>();
                    failureResponse.put("status", "failure");
                    failureResponse.put("message", "Payment is not successful");
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(failureResponse);
                }
            } else {
                Map<String, Object> notFoundResponse = new HashMap<>();
                notFoundResponse.put("status", "error");
                notFoundResponse.put("message", "Payment data not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String createPayment(
                                @RequestParam("amount") double amount,
                                @RequestParam("note") String note,
                                @RequestParam("first_name") String firstName,
                                @RequestParam("last_name") String lastName,
                                @RequestParam("email") String email,
                                @RequestParam("phone") String phone) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token 86bcdfec6a54e163bfe3f51921d36782d96697ae");

        // Construct the request payload
        String requestPayload = "{\"vendor\": " + 1 + ", \"amount\": " + amount + ", \"note\": \"" + note + "\", " +
                "\"first_name\": \"" + firstName + "\", \"last_name\": \"" + lastName + "\", " +
                "\"email\": \"" + email + "\", \"phone\": \"" + phone +"\"," +
                "\"return_url\": \"google.tn\", " +
                "\"cancel_url\": \"google.tn\", \"webhook_url\": \"https://www.webhook_url.tn\", " +
                "\"order_id\": \"244557\"}";
        ;

        HttpEntity<String> entity = new HttpEntity<>(requestPayload, headers);

        // Make the POST request
        ResponseEntity<String> response = restTemplate.exchange( "https://sandbox.paymee.tn/api/v2/payments/create", HttpMethod.POST, entity, String.class);


        return response.getBody();
    }


}

