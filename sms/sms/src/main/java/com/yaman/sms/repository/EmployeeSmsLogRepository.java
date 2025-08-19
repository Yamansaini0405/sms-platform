package com.yaman.sms.repository;

import com.yaman.sms.entity.EmployeeSmsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeSmsLogRepository extends JpaRepository<EmployeeSmsLog, Long> {

    // Find all failed messages for retry
    List<EmployeeSmsLog> findByStatus(EmployeeSmsLog.Status status);

    // Find by phone number (optional, for debugging)
    List<EmployeeSmsLog> findByPhoneNumber(String phoneNumber);
}
