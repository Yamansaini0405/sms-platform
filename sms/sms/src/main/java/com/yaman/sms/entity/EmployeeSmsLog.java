package com.yaman.sms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_sms_log")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeeSmsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeName;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private SmsType smsType;   // ATTENDANCE or SALARY

    @Lob
    private String message;

    @Enumerated(EnumType.STRING)
    private Status status;    // SUCCESS or FAILED

    private String failureReason;

    private int retryCount = 0;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum SmsType {
        ATTENDANCE, SALARY
    }

    public enum Status {
        SUCCESS, FAILED
    }
}
