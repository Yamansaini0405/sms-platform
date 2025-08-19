package com.yaman.sms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterAdminRequest {
    private String username;
    private String password;
}
