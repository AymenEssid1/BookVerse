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
    public void markAllMessagesAsRead(Integer chatId) {
        List<Message> unreadMessages = messageRepository.findByChatIdAndRead(chatId, 0);
        for (Message message : unreadMessages) {
            message.setRead(1);
            messageRepository.save(message);
        }
    }

    @Override
    public Message addMessage(Integer senderId, Integer chatId, String content) throws ChatNotFoundException, UserNotFoundException,NotPartOfChatException {
        User sender = userService.getUserById(senderId);
       System.out.println(sender.getId());

        Chat chat = chatService.findChatById(chatId);
       // System.out.println("chatid:"+chat.getId());
       // System.out.println("user1 id :"+chat.getUser1().getId());
        //System.out.println("user2 id :"+chat.getUser2().getId());

        if(chat.getUser1().getId().equals(sender.getId()) || chat.getUser2().getId().equals(sender.getId())){
            System.out.println("good");
            Message message = new Message();
            message.setSender(sender);
            message.setChat(chat);
            message.setContent(content);
            message.encryptContent();
            System.out.println(message);
            return messageRepository.save(message);
        }
        else {
            System.out.println(chat.getUser1().getId().equals(sender.getId()) || chat.getUser2().getId().equals(sender.getId()));
            throw new NotPartOfChatException();}
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
