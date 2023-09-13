package com.apiotrowska.restservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    @Min(value = 1800, message = "The publication year should be greater than 1800")
    @Max(value = 2024, message = "The publication year can not be greater than 2024")
    private int publicationYear;
    @NotNull(message = "The number of pages is required")
    @Min(value = 10, message = "The number of pages should be greater than 10")
    @Max(value = 5000, message = "The number of pages can not be greater than 5000")
    private int pages;
}
