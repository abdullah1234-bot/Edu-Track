package com.fifth_semester.project.services;

import com.fifth_semester.project.entities.Fee;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.repositories.FeeRepository;
import com.fifth_semester.project.repositories.StudentRepository;
import com.fifth_semester.project.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FeeService {

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private StudentRepository studentRepository;

    // Get all fees for a student
    public List<Fee> getFeesForStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Student student = studentRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return feeRepository.findByStudent(student);
    }

    // Get specific fee for a student by period
    public Fee getFeeByStudentAndPeriod(String period) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Student student = studentRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return feeRepository.findByStudentAndPeriod(student, period)
                .orElseThrow(() -> new RuntimeException("Fee statement not found for the specified period."));
    }

    // Make a fee payment (transactional to ensure consistency)
    @Transactional
    public String makeFeePayment(Long feeId, Double amount) {
        Fee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new RuntimeException("Fee record not found"));
        double updatedPaidAmount;
        double updatedDueAmount;
        if (fee.getDueAmount() <= 0) {
            return "Fee already paid in full.";
        }
        else if(fee.getDueAmount() < amount) {
            updatedPaidAmount = fee.getTotalAmount();
            double temp = amount - fee.getDueAmount();
            updatedDueAmount = 0;
            fee.setPaidAmount(updatedPaidAmount);
            fee.setDueAmount(updatedDueAmount);
            fee.setStatus("Paid");
            feeRepository.save(fee);
            return "Payment successful. Cashback: " + temp;
        }

        updatedPaidAmount = fee.getPaidAmount() + amount;
        updatedDueAmount = fee.getTotalAmount() - updatedPaidAmount;

        fee.setPaidAmount(updatedPaidAmount);
        fee.setDueAmount(updatedDueAmount);

        if (updatedDueAmount <= 0) {
            fee.setStatus("Paid");
        } else {
            fee.setStatus("Partially Paid");
        }

        feeRepository.save(fee);

        return "Payment successful. Remaining due amount: " + updatedDueAmount;
    }

    // Admins create or update fee structure for a student
//    @Transactional
//    public Fee createOrUpdateFee(Long studentId, Double totalAmount, String period) {
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        Optional<Fee> existingFee = feeRepository.findByyStudentAndPeriod(student, period);
//
//        Fee fee;
//        if (existingFee.isPresent()) {
//            fee = existingFee.get();
//            fee.setTotalAmount(totalAmount);
//            fee.setDueAmount(totalAmount - fee.getPaidAmount());
//        } else {
//            fee = new Fee(student, totalAmount, 0.0, totalAmount, "Unpaid", period);
//            student.getFees().add(fee);
//        }
//
//        studentRepository.save(student);
//        feeRepository.save(fee);
//        return fee;
//    }
    @Transactional
    public Fee createOrUpdateFee(Long studentId, Double totalAmount, String period) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Normalize period for consistency
        period = period.toLowerCase();

        Optional<Fee> existingFee = feeRepository.findByStudentAndPeriod(student, period);

        Fee fee;
        if (existingFee.isPresent()) {
            // Update existing fee
            fee = existingFee.get();
            fee.setTotalAmount(totalAmount);
            fee.setDueAmount(totalAmount - fee.getPaidAmount());
        } else {
            // Create a new fee
            fee = new Fee(student, totalAmount, 0.0, totalAmount, "Unpaid", period);
            student.getFees().add(fee);  // Maintain bidirectional relationship
        }

        feeRepository.save(fee);
        return fee;
    }

    // Get all fee records for admin reporting
    public List<Fee> getAllFees() {
        return feeRepository.findAll();
    }

    // Get all unpaid fees for a student
    public List<Fee> getUnpaidFeesForStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Student student = studentRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return feeRepository.findByStudentAndStatusIn(student, List.of("Unpaid", "Partially Paid"));
    }

    // Get the latest unpaid fee for a student
    public Fee getLatestUnpaidFeeForStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Student student = studentRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return feeRepository.findTopByStudentAndStatusInOrderByPeriodDesc(student, List.of("Unpaid", "Partially Paid"))
                .orElseThrow(() -> new RuntimeException("No unpaid fee found for the student"));
    }

}
