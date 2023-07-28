package com.aymen.security.book;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewInfoDTO {
    private Integer userId;
    private Integer bookId;
    private Integer rating;

}
