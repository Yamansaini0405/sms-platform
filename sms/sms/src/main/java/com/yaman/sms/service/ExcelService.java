package com.yaman.sms.service;

import com.yaman.sms.entity.EmployeeSmsLog;
import com.yaman.sms.repository.EmployeeSmsLogRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final SmsService smsService;
    private final EmployeeSmsLogRepository smsLogRepository;

    public void uploadAndSend(MultipartFile file, String type) throws Exception {
        InputStream is = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        List<EmployeeSmsLog> logs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // skip header
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String employeeName = row.getCell(0).getStringCellValue();
            String phoneNumber = row.getCell(1).getStringCellValue();

            String message = "";
            EmployeeSmsLog.SmsType smsType;

            if ("ATTENDANCE".equalsIgnoreCase(type)) {
                smsType = EmployeeSmsLog.SmsType.ATTENDANCE;
                String status = row.getCell(2).getStringCellValue(); // Present / Absent
                message = String.format("Hi %s, your attendance status today is %s.", employeeName, status);
            } else if ("SALARY".equalsIgnoreCase(type)) {
                smsType = EmployeeSmsLog.SmsType.SALARY;
                message = String.format("Hi %s, your salary of Rs 1400 has been credited.", employeeName);
            } else {
                throw new RuntimeException("Invalid SMS type: " + type);
            }

            EmployeeSmsLog log = smsService.sendSms(employeeName, phoneNumber, message, smsType);
            logs.add(log);
        }

        workbook.close();
        smsLogRepository.saveAll(logs);
    }
}