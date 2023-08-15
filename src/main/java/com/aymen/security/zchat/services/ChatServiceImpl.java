package com.aymen.security.zchat.services;

import com.aymen.security.user.User;
import com.aymen.security.user.UserService;
import com.aymen.security.zchat.Chat;
import com.aymen.security.zchat.exceptions.ChatAlreadyExistException;
import com.aymen.security.zchat.exceptions.ChatNotFoundException;
import com.aymen.security.zchat.exceptions.NoChatExistsInTheRepository;
import com.aymen.security.zchat.exceptions.UserNotFoundException;
import com.aymen.security.zchat.repository.ChatRepository;
import com.aymen.security.zchat.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Chat addChat(Integer id1,Integer id2) throws ChatAlreadyExistException, UserNotFoundException {
        if(chatRepository.findChatByUserIds(id1, id2)==null){

            Chat chat = new Chat();
            User user1= userService.getUserById(id1);
            User user2= userService.getUserById(id2);
            if(user1!=null && user2!=null)
            {chat.setUser1(user1);
            chat.setUser2(user2);
            return chatRepository.save(chat);}
            else{throw new UserNotFoundException();}
        }
        else {throw new ChatAlreadyExistException();}

    }


    @Override
    public List<Chat> findallchats() throws NoChatExistsInTheRepository {
        if (chatRepository.findAll().isEmpty()) {
            throw new NoChatExistsInTheRepository();
        } else {
            return chatRepository.findAll();
        }

    }

    @Override
    public Chat findChatById(Integer id) throws ChatNotFoundException {
        return chatRepository.findById(id)
                .orElseThrow(() -> new ChatNotFoundException());
    }

    @Override
    public void deleteChatById(Integer id) throws ChatNotFoundException {
        if (chatRepository.existsById(id)) {
            chatRepository.deleteById(id);
        } else {
            throw new ChatNotFoundException();
        }

    }

    @Override
    public Chat findChatByBothUsers(Integer id1, Integer id2) throws ChatAlreadyExistException, UserNotFoundException {
        Chat chat =chatRepository.findChatByUserIds(id1,id2);
        if( chat ==null){
            return this.addChat(id1,id2);
        }
        return chat;
    }

    @Override
    public List<Chat> findAdminChats(Integer id1) {
       List<Chat> chats= chatRepository.findAdmiChats(id1);
       return chats;
    }


}
