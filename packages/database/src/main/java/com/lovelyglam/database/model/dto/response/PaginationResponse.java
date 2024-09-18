package com.lovelyglam.database.model.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse <T> {
    private List<T> content;
    private int page;
    private int pageSize;
    private int totalResult;
    private int totalPage;
    private boolean isFirstPage;
    private boolean isLastPage;
}
