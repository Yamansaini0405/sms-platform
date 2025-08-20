package com.yaman.sms.controller;

import com.yaman.sms.dto.SmsRequest;
import com.yaman.sms.entity.EmployeeSmsLog;
import com.yaman.sms.service.ExcelService;
import com.yaman.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {

    private final ExcelService excelService;
    private final SmsService smsService;

    // 1️⃣ Upload Excel & send SMS (Attendance or Salary)
    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type
    ) {
        try {
            excelService.uploadAndSend(file, type);
            return ResponseEntity.ok("SMS processed successfully for type: " + type);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // 2️⃣ Retry all failed SMS
    @PostMapping("/retry/failed")
    public ResponseEntity<String> retryFailedSms() {
        smsService.retryFailedSms();
        return ResponseEntity.ok("Retry triggered for all failed SMS.");
    }

    // 3️⃣ Retry single SMS by logId
    @PostMapping("/retry/{logId}")
    public ResponseEntity<?> retrySingleSms(@PathVariable Long logId) {
        try {
            EmployeeSmsLog updatedLog = smsService.retrySingleSms(logId);
            return ResponseEntity.ok(updatedLog);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // 4️⃣ Optional: Get all SMS logs
//    @GetMapping("/logs")
//    public ResponseEntity<List<EmployeeSmsLog>> getAllSmsLogs() {
//        return ResponseEntity.ok(smsService.getAllLogs());
//    }
}