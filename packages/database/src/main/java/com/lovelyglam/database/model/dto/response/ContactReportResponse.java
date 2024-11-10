package com.lovelyglam.database.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactReportResponse {
    private String id;
    private String contactName;
    private String email;
    private String message;
    private String feedback;
}
