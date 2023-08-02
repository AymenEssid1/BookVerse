package com.aymen.security.zchat.services;



import com.aymen.security.zchat.Chat;
import com.aymen.security.zchat.exceptions.ChatAlreadyExistException;
import com.aymen.security.zchat.exceptions.ChatNotFoundException;
import com.aymen.security.zchat.exceptions.NoChatExistsInTheRepository;
import com.aymen.security.zchat.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ChatService {

    public Chat addChat(Integer id1 ,Integer id2) throws ChatAlreadyExistException, UserNotFoundException;

    List<Chat> findallchats() throws NoChatExistsInTheRepository;

    public Chat findChatById(Integer id1) throws ChatNotFoundException;

    void deleteChatById(Integer id) throws ChatNotFoundException;



}
