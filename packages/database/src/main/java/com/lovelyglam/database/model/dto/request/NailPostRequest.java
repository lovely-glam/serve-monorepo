package com.lovelyglam.database.model.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NailPostRequest {
    private String title;
    private String description;
    private String content;
    private List<String> images;
}
