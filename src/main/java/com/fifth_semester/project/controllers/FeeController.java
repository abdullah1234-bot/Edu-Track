package com.fifth_semester.project.controllers;

import com.fifth_semester.project.entities.Fee;
import com.fifth_semester.project.services.FeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fees")
@Tag(name = "Fee APIs")
public class FeeController {

    @Autowired
    private FeeService feeService;

    // Create or update fee structure (admin access)
    @PostMapping("/admin/student/{studentId}/create-or-update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Fee> createOrUpdateFee(@PathVariable Long studentId,
                                                 @RequestParam Double totalAmount,
                                                 @RequestParam String period) {
        Fee fee = feeService.createOrUpdateFee(studentId, totalAmount, period);
        return ResponseEntity.ok(fee);
    }

    // Get all fee statements for a specific student
    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Fee>> getFeesForStudent() {
        List<Fee> fees = feeService.getFeesForStudent();
        return ResponseEntity.ok(fees);
    }

    // Get fee statement by student and period
    @GetMapping("/student/period")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Fee> getFeeByStudentAndPeriod(@RequestParam String period) {
        Fee fee = feeService.getFeeByStudentAndPeriod(period);
        return ResponseEntity.ok(fee);
    }

    // Make a payment for a specific fee record
    @PostMapping("/payment/{feeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> makeFeePayment(@PathVariable Long feeId, @RequestParam Double amount) {
        String result = feeService.makeFeePayment(feeId, amount);
        return ResponseEntity.ok(result);
    }

    // Get all fee records for reporting (admin access)
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Fee>> getAllFees() {
        List<Fee> fees = feeService.getAllFees();
        return ResponseEntity.ok(fees);
    }

    // Get all unpaid fees for a student
    @GetMapping("/student/unpaid")
    public ResponseEntity<List<Fee>> getUnpaidFeesForStudent() {
        List<Fee> unpaidFees = feeService.getUnpaidFeesForStudent();
        return ResponseEntity.ok(unpaidFees);
    }

    // Get the latest unpaid fee for a student
    @GetMapping("/student/unpaid/latest")
    public ResponseEntity<Fee> getLatestUnpaidFeeForStudent() {
        Fee latestUnpaidFee = feeService.getLatestUnpaidFeeForStudent();
        return ResponseEntity.ok(latestUnpaidFee);
    }


}
