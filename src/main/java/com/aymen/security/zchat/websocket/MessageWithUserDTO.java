package com.aymen.security.zchat.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageWithUserDTO {
    private Integer id;
    private Date time;
    private String content;
    private String Email; // Include the email of the user
    private Integer chatid;

    // Constructors, getters, and setters
}
