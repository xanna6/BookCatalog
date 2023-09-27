package com.apiotrowska.databaseservice.filter;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookFilterDto {

    private List<BookFilter> bookFilters;
}
