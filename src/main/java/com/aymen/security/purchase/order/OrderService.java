package com.aymen.security.purchase.order;


import com.aymen.security.book.Book;
import com.aymen.security.book.BookService;
import com.aymen.security.purchase.cart.Cart;
import com.aymen.security.purchase.cart.CartService;
import com.aymen.security.purchase.item.Item;
import com.aymen.security.user.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

@Autowired
   CartService cartService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    BookService bookService;

    public List<Order> getOrders(Integer id){
       return orderRepository.findByUserId(id);


    }

    public void purchaseComplete(Integer order1){
       Order order= orderRepository.getById(order1);

        order.setPaymentStatus(PaymentStatus.PAID);
        orderRepository.save(order);
        cartService.emptyCart(order.getUser().getId());

    }

    public  OrderResponse createOrder(User user) {
        Cart cart = cartService.getCartByUser(user.getId());
        List<Item> cartItems = cart.getItems();
        Map<Book, Integer> itemMap = new HashMap<>();
        for (Item item : cartItems) {
            Book book = item.getBook();
            int quantity = item.getQuantity();

            if(!bookService.checkQuant(book.getId(),quantity)){
                return null;
            }

            itemMap.put(book, quantity);
        }
       // System.out.println(itemMap);
        // Create the order
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .items(itemMap)
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        // REduce quant
        for (Map.Entry<Book, Integer> entry : order.getItems().entrySet()) {
            Book book = entry.getKey();
            Integer value = entry.getValue();
            bookService.reduceQuant(book.getId(),value);

        }
        Order savedOrder = orderRepository.save(order);

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Book, Integer> entry : order.getItems().entrySet()) {
            Book book = entry.getKey();
            Integer value = entry.getValue();
            stringBuilder
                    .append(value)
                    .append("X ")
                    .append(book.getName())
                    .append("📘");
        }

        String result = stringBuilder.toString();

       // System.out.println(result);

        // Parse the JSON response
        String responseBody = this.createPayment(cart.getTotalPrice(),result,user.getFirstname(), user.getLastname(), user.getEmail());
        JSONObject json = new JSONObject(responseBody);
       // System.out.println(json);

        // Extract specific fields from the JSON object
        String notee = json.getJSONObject("data").getString("note");
        String phone = json.getJSONObject("data").getString("phone");
        String token = json.getJSONObject("data").getString("token");
        String email = json.getJSONObject("data").getString("email");
        String payment_url = json.getJSONObject("data").getString("payment_url");
        String message = json.getString("message");
        // Extract other fields as needed

        // Use the extracted fields as required
        System.out.println("Note: " + notee);
        System.out.println("Phone: " + phone);
        System.out.println("Token: " + token);
        System.out.println("Message: " + message);
        System.out.println("payURL: " + payment_url);

        OrderResponse orderResponse = new OrderResponse(order, payment_url,token);
        return orderResponse;
    }




    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    public String createPayment(
             double amount,
             String note,
             String firstName,
             String lastName,
             String email/*,
            @RequestParam("phone") String phone*/) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token 86bcdfec6a54e163bfe3f51921d36782d96697ae");

        // Construct the request payload
        String requestPayload = "{\"vendor\": " + 1 + ", \"amount\": " + amount + ", \"note\": \"" + note + "\", " +
                "\"first_name\": \"" + firstName + "\", \"last_name\": \"" + lastName + "\", " +
                "\"email\": \"" + email + "\", \"phone\": \"" + "+21621354227" +"\"," +
                "\"return_url\": \"https://localhost:4200/front-page\", " +
                "\"cancel_url\": \"https://localhost:4200/front-page\", \"webhook_url\": \"https://www.webhook_url.tn\", " +
                "\"order_id\": \"244557\"}";
        ;

        HttpEntity<String> entity = new HttpEntity<>(requestPayload, headers);

        // Make the POST request
        ResponseEntity<String> response = restTemplate.exchange( "https://sandbox.paymee.tn/api/v2/payments/create", HttpMethod.POST, entity, String.class);


        return response.getBody();
    }

    public void deleteOrder(Integer orderid) {
        Order order= orderRepository.getById(orderid);
        for (Map.Entry<Book, Integer> entry : order.getItems().entrySet()) {
            Book book = entry.getKey();
            Integer value = entry.getValue();
            bookService.increaseQuant(book.getId(),value);

        }
        orderRepository.deleteById(orderid);
    }
}
