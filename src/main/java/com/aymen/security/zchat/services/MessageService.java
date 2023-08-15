package com.aymen.security.zchat.services;

import com.aymen.security.zchat.Message;
import com.aymen.security.zchat.exceptions.ChatNotFoundException;
import com.aymen.security.zchat.exceptions.NotPartOfChatException;
import com.aymen.security.zchat.exceptions.UserNotFoundException;

import java.util.List;

public interface MessageService {

    void markAllMessagesAsRead(Integer chatId);

    Message addMessage(Integer senderId, Integer chatId, String content) throws ChatNotFoundException, UserNotFoundException, NotPartOfChatException;
    List<Message> getMessagesByChatId(Integer chatId) throws ChatNotFoundException;
}
