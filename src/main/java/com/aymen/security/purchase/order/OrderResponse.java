package com.aymen.security.purchase.order;


import lombok.Data;

@Data
public class OrderResponse {
    private Order order;
    private String url;
    private String token;

    public OrderResponse(Order order, String url,String token) {
        this.order = order;
        this.url = url;
        this.token=token;
    }

}
