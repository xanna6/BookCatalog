package com.apiotrowska.databaseservice.dto;

import lombok.*;

import java.time.Year;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private Year publicationYear;
    private int pages;
}
