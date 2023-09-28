package com.apiotrowska.common.dto;

import com.apiotrowska.common.model.BookFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookFilterDto {

    private List<BookFilter> bookFilters;
}
