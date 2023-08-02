package com.aymen.security.zchat.websocket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Controller
@EnableScheduling
public class WebSocketController {

    private final Logger logger = LoggerFactory.getLogger(WebSocketController.class);


    @MessageMapping("/send")
    @SendTo("/app/bozo")
    public String handleSendMessage(String message) {
        logger.info("Received message: {}", message);
        return message;
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Method to send a message from the backend to the frontend
    public void sendToClient(String message) {
        messagingTemplate.convertAndSend("/topic", message);
    }

    // Method to be executed every 5 seconds
    @Scheduled(fixedDelay = 5000) // 5000 milliseconds = 5 seconds
    public void scheduledSendMessage() {
        String message = "Hello from the backend!"; // Modify the message as needed

        sendToClient(message);
        //System.out.println("start");
    }

}
