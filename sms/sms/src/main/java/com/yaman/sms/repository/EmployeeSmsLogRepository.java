package com.yaman.sms.repository;

import com.yaman.sms.entity.EmployeeSmsLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeSmsLogRepository extends JpaRepository<EmployeeSmsLog, Long> {
    List<EmployeeSmsLog> findByStatus(EmployeeSmsLog.Status status);
}
