package com.yaman.sms.service;


import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeeExcelDTO {
    private String employeeName;
    private String phoneNumber;
}