package com.aymen.security.zchat.websocket;


import com.aymen.security.zchat.Message;
import com.aymen.security.zchat.exceptions.ChatNotFoundException;
import com.aymen.security.zchat.exceptions.NotPartOfChatException;
import com.aymen.security.zchat.exceptions.UserNotFoundException;
import com.aymen.security.zchat.repository.MessageRepository;
import com.aymen.security.zchat.services.MessageService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Controller
@EnableScheduling
public class WebSocketController {




    private final Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MessageService messageService;
    @Autowired
    private MessageRepository messageRepository;


    @Transactional
    @MessageMapping("/send")
   // @SendTo("/app/bozo")
    public void handleSendMessage(String message) throws IllegalAccessException {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);

            // Extract each field from the JSON object
            Integer chatId = jsonNode.get("chatId").asInt();
            String messageText = jsonNode.get("message").asText();
            Integer senderId = jsonNode.get("senderId").asInt();

            if(senderId==0){
               messageService.markAllMessagesAsRead(chatId);
                List<Message> messages = messageRepository.findByChatIdAndRead(chatId, 1);
                for (Message messagee : messages) {
                    messagee.encryptContent(); // Re-encrypt the content
                }
               logger.info("reaaaaaaaaaaaaaaaaaaaaaaaaaaaaaad");
               return;
            }
            // Now you can use these extracted fields as per your requirements
            logger.info("Chat ID: " + chatId);
            logger.info("Message: " + messageText);
            logger.info("Sender ID: " + senderId);



                Message message2 = messageService.addMessage(senderId,chatId,messageText);
                logger.info(message2.toString());
                sendToClient(message2);
            }
            catch (UserNotFoundException e) {
                logger.info("user 404 error");
            }catch (ChatNotFoundException es){
                logger.info("chat 404 error");
            }catch(NotPartOfChatException e){
                logger.info("not part of chat error");
            }catch (Exception e) {

            logger.error("An error occurred:", e);

            }
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendToClient(Message message) {
        // Create a copy of the Message object to avoid modifying the original
        Message decryptedMessage = new Message();
        decryptedMessage.setId(message.getId());
        decryptedMessage.setTime(message.getTime());
        decryptedMessage.setContent(message.getContent());
        decryptedMessage.setSender(message.getSender());
        decryptedMessage.setChat(message.getChat());

        // Decrypt the content of the copied Message object
        decryptedMessage.decryptContent();

        // Convert Message to MessageWithUserDTO
        MessageWithUserDTO messageDTO = convertToMessageWithUserDTO(decryptedMessage);
        Integer chatId = message.getChat().getId();
        System.out.println(chatId);

        // Send the MessageWithUserDTO over the WebSocket
        messagingTemplate.convertAndSend("/topic/room/"+ chatId, messageDTO);
    }


    private MessageWithUserDTO convertToMessageWithUserDTO(Message message) {

        message.decryptContent();

        // Create the MessageWithUserDTO using the decrypted content
        MessageWithUserDTO messageDTO = new MessageWithUserDTO();
        messageDTO.setId(message.getId());
        messageDTO.setTime(message.getTime());
        messageDTO.setContent(message.getContent());
        messageDTO.setChatid(message.getChat().getId());

        // Include the email of the user (sender)
        messageDTO.setEmail(message.getSender().getEmail());

        return messageDTO;
    }



    // Method to be executed every 5 seconds
    //@Scheduled(fixedDelay = 5000) // 5000 milliseconds = 5 seconds
   /* public void scheduledSendMessage() {
        String message = "Hello from the backend!"; // Modify the message as needed

        sendToClient(message);
        //System.out.println("start");
    }*/

}
