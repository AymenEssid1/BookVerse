package com.aymen.security.zchat.services;

import com.aymen.security.user.User;
import com.aymen.security.user.UserService;
import com.aymen.security.zchat.Chat;
import com.aymen.security.zchat.Message;
import com.aymen.security.zchat.exceptions.ChatNotFoundException;
import com.aymen.security.zchat.exceptions.NotPartOfChatException;
import com.aymen.security.zchat.exceptions.UserNotFoundException;
import com.aymen.security.zchat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MessageServiceImpl implements  MessageService{

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Override
    public Message addMessage(Integer senderId, Integer chatId, String content) throws ChatNotFoundException, UserNotFoundException,NotPartOfChatException {
        User sender = userService.getUserById(senderId);

        Chat chat = chatService.findChatById(chatId);

        if(chat.getUser1()==sender||chat.getUser2()==sender){
            Message message = new Message();
            message.setSender(sender);
            message.setChat(chat);
            message.setContent(content);
            message.encryptContent();
            return messageRepository.save(message);
        }
        else throw new NotPartOfChatException();
    }

    @Override
    public List<Message> getMessagesByChatId(Integer chatId) throws ChatNotFoundException {
        Chat chat = chatService.findChatById(chatId);
        List<Message> messages = messageRepository.findByChatOrderByTime(chat);
        for (Message message : messages) {
            message.decryptContent();
        }
        return messages;
    }
}
