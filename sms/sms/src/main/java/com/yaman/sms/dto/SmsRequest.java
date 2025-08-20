package com.yaman.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequest {
    private String type; // "ATTENDANCE" or "SALARY"
}
