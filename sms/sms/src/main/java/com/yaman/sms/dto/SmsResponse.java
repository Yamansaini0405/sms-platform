package com.yaman.sms.dto;

import com.yaman.sms.entity.EmployeeSmsLog;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SmsResponse {
    private Long id;
    private String employeeName;
    private String phoneNumber;
    private String message;
    private EmployeeSmsLog.SmsType smsType;
    private EmployeeSmsLog.Status status;
    private String failureReason;
    private int retryCount;
}

