package com.lovelyglam.database.model.dto.request;

import lombok.Data;

@Data
public class CustomerRegisterRequest {
    private String username;
    private String email;
    private String password;
    private String rePassword;
}
