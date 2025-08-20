package com.yaman.sms.service;

import com.yaman.sms.entity.EmployeeSmsLog;
import com.yaman.sms.repository.EmployeeSmsLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final EmployeeSmsLogRepository smsLogRepository;
    private final RestTemplate restTemplate;

    private static final String FAST2SMS_API_KEY = "YOUR_FAST2SMS_API_KEY";
    private static final String FAST2SMS_URL = "https://www.fast2sms.com/dev/bulkV2";

    public EmployeeSmsLog sendSms(String employeeName, String phoneNumber, String message, EmployeeSmsLog.SmsType type) {

        EmployeeSmsLog log = EmployeeSmsLog.builder()
                .employeeName(employeeName)
                .phoneNumber(phoneNumber)
                .smsType(type)
                .message(message)
                .status(EmployeeSmsLog.Status.FAILED)
                .retryCount(0)
                .build();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("authorization", FAST2SMS_API_KEY);

            Map<String, Object> body = new HashMap<>();
            body.put("route", "v3");
            body.put("sender_id", "TXTIND");
            body.put("message", message);
            body.put("language", "english");
            body.put("flash", 0);
            body.put("numbers", phoneNumber);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    FAST2SMS_URL, HttpMethod.POST, entity, String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.setStatus(EmployeeSmsLog.Status.SUCCESS);
            } else {
                log.setFailureReason("HTTP Error: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.setFailureReason(e.getMessage());
        }

        return smsLogRepository.save(log);
    }

    // Retry logic for single SMS
    public EmployeeSmsLog retrySingleSms(Long logId) {
        EmployeeSmsLog log = smsLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("SMS log not found with id " + logId));

        if (log.getStatus() == EmployeeSmsLog.Status.SUCCESS) {
            throw new RuntimeException("SMS already sent successfully.");
        }

        if (log.getRetryCount() >= 3) {
            throw new RuntimeException("Max retry attempts reached.");
        }

        log.setRetryCount(log.getRetryCount() + 1);

        EmployeeSmsLog newLog = sendSms(log.getEmployeeName(), log.getPhoneNumber(), log.getMessage(), log.getSmsType());
        newLog.setRetryCount(log.getRetryCount());

        return smsLogRepository.save(newLog);
    }

    // Retry all failed SMS
    public void retryFailedSms() {
        var failedLogs = smsLogRepository.findByStatus(EmployeeSmsLog.Status.FAILED);
        for (EmployeeSmsLog log : failedLogs) {
            if (log.getRetryCount() < 3) {
                log.setRetryCount(log.getRetryCount() + 1);
                sendSms(log.getEmployeeName(), log.getPhoneNumber(), log.getMessage(), log.getSmsType());
            }
        }
    }
}