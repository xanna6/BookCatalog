package com.apiotrowska.databaseservice.filter;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookFilter {

    private String filterKey;
    private Object value;
}
