package com.apiotrowska.common.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "The title can have a maximum of 100 characters.")
    private String title;
    @NotBlank(message = "Author is required")
    @Size(max = 50, message = "The author's name and surname can have a maximum of 50 characters in total")
    private String author;
    @NotNull(message = "The publication year is required")
    @PastOrPresent(message = "The publication year should be past or present")
    private Year publicationYear;
    @NotNull(message = "The number of pages is required")
    @Min(value = 1, message = "The number of pages should be greater than 0")
    @Max(value = 30000, message = "The number of pages can not be greater than 30000")
    private int pages;
}
