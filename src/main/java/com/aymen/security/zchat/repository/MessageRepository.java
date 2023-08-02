package com.aymen.security.zchat.repository;

import com.aymen.security.zchat.Chat;
import com.aymen.security.zchat.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    public List<Message> findByChatOrderByTime(Chat chat);
}