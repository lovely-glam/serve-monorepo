package com.lovelyglam.database.model.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
public class ResponseObject {
    private Object content;
    private String code;
    private String message;
    private boolean isSuccess;
    private LocalDateTime requestTime;
    private HttpStatus status;
}
