package com.aymen.security.zchat;

import com.aymen.security.zchat.exceptions.*;
import com.aymen.security.zchat.services.ChatService;
import com.aymen.security.zchat.services.MessageService;
import com.aymen.security.zchat.websocket.MessageWithUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/add")
    public ResponseEntity<Chat> addChat(@RequestParam Integer id1, @RequestParam Integer id2) {
        try {
            Chat chat = chatService.addChat(id1, id2);
            return ResponseEntity.ok(chat);
        } catch (ChatAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Chat>> getAllChats() {
        try {
            List<Chat> chats = chatService.findallchats();
            return ResponseEntity.ok(chats);
        } catch (NoChatExistsInTheRepository e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/adminChat")
    public ResponseEntity<List<Chat>> getAdminChats(@RequestParam Integer id) {

            List<Chat> chats = chatService.findAdminChats(id);
            return ResponseEntity.ok(chats);
        }


    @GetMapping("/getById")
    public ResponseEntity<Chat> getChatById(@RequestParam Integer id) {
        try {
            Chat chat = chatService.findChatById(id);
            return ResponseEntity.ok(chat);
        } catch (ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/getByBoth")
    public ResponseEntity<Chat> getChatByBoth(@RequestParam Integer id1, @RequestParam Integer id2) {
        try {
            Chat chat = chatService.findChatByBothUsers(id1, id2);
            return ResponseEntity.ok(chat);
        } catch (ChatAlreadyExistException e) {
            // Handle the ChatAlreadyExistException
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Return a 409 Conflict status code, for example.

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteChatById(@RequestParam Integer id) {
        try {
            chatService.deleteChatById(id);
            return ResponseEntity.ok("deleted");
        } catch (ChatNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @Autowired
    private MessageService messageService;

    @PostMapping("/addMessage")
    public ResponseEntity<Message> addMessage(@RequestParam Integer senderId, @RequestParam Integer chatId, @RequestParam String content) {
        try {
            Message message = messageService.addMessage(senderId, chatId, content);
            return ResponseEntity.ok(message);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (ChatNotFoundException es){
            return ResponseEntity.notFound().build();
        }catch(NotPartOfChatException e){
            return  ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<MessageWithUserDTO>> getMessagesByChatId(@RequestParam Integer chatId) {
        try {
            List<Message> messages = messageService.getMessagesByChatId(chatId);

            // Convert List<Message> to List<MessageWithUserDTO>
            List<MessageWithUserDTO> messageDTOs = convertToMessageWithUserDTOList(messages);

            return new ResponseEntity<>(messageDTOs, HttpStatus.OK);
        } catch (ChatNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Helper method to convert List<Message> to List<MessageWithUserDTO>
    private List<MessageWithUserDTO> convertToMessageWithUserDTOList(List<Message> messages) {
        List<MessageWithUserDTO> messageDTOs = new ArrayList<>();

        for (Message message : messages) {
            MessageWithUserDTO messageDTO = new MessageWithUserDTO();
            messageDTO.setId(message.getId());
            messageDTO.setTime(message.getTime());
            messageDTO.setContent(message.getContent());

            // Include the email of the user (sender)
            messageDTO.setEmail(message.getSender().getEmail());

            messageDTOs.add(messageDTO);
        }

        return messageDTOs;
    }


}
