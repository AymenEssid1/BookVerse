package com.aymen.security.controllers;

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
@Controller
@RequestMapping("/payment")
public class PaymeePaymentController {

    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();
    @Autowired
    OrderService orderService;



    @RequestMapping(value = "check", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> checkPaymentStatus(@RequestParam Integer orderid ,@RequestParam String token) {
        String url = "https://sandbox.paymee.tn/api/v1/payments/" + token + "/check";

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token 86bcdfec6a54e163bfe3f51921d36782d96697ae");

        String requestPayload = "{}";
        HttpEntity<String> entity = new HttpEntity<>(requestPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            JSONObject json = new JSONObject(responseBody);

            boolean status = json.getBoolean("status");
            if (status) {
                boolean paymentStatus = json.getJSONObject("data").getBoolean("payment_status");
                if (paymentStatus) {

                    orderService.purchaseComplete(orderid);


                    return ResponseEntity.status(HttpStatus.OK).body("Payment is successful");
                } else {
                    orderService.deleteOrder(orderid);

                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Payment is not successful");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
                "\"return_url\": \"https://localhost:4200/front-page\", " +
                "\"cancel_url\": \"https://www.cancel_url.tn\", \"webhook_url\": \"https://www.webhook_url.tn\", " +
                "\"order_id\": \"244557\"}";
        ;

        HttpEntity<String> entity = new HttpEntity<>(requestPayload, headers);

        // Make the POST request
        ResponseEntity<String> response = restTemplate.exchange( "https://sandbox.paymee.tn/api/v2/payments/create", HttpMethod.POST, entity, String.class);


        return response.getBody();
    }


}

