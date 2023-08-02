package com.aymen.security.zchat;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/send")
    @SendTo("/app/chat")
    public String handleSendMessage(String message) {
        logger.info("Received message: {}", message);
        return message;
    }

}
