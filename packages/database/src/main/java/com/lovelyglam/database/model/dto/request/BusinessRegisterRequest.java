package com.lovelyglam.database.model.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BusinessRegisterRequest {
    @NotBlank(message = "Username Must Not Be Empty")
    private String username;
    @NotBlank(message = "Password Must Not Be Empty")
    private String password;
    @NotBlank(message = "RePassword Must Not Be Empty")
    private String rePassword;
    @NotBlank(message = "Business Email Must Not Be Empty")
    private String email;
    @NotBlank(message = "Name Must Not Be Empty")
    private String name;
    private String ownerFirstName;
    @NotBlank(message = "Owner Last Name Must Not Be Empty")
    private String ownerLastName;
    @NotBlank(message = "Personal Identity Must Not Be Empty")
    private String ownerPersonalIdentity;
    @NotBlank(message = "Business Code Must Not Be Empty")
    private String businessCode;
    @NotBlank(message = "Shop Avatar Must Have")
    private String avatarUrl;
    @NotNull(message = "Need To Add thumpNail")
    private List<String> thumpNails;
    @NotBlank(message = "Description Must Be Enter")
    private String description;
    @NotBlank(message = "Need to enter address")
    private String address;
}
