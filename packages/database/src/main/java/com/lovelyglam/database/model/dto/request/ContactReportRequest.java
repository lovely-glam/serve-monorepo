package com.lovelyglam.database.model.dto.request;

import lombok.Data;

@Data
public class ContactReportRequest {
    private String contactName;
    private String email;
    private String message;
}
