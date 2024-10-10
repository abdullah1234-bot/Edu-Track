package com.fifth_semester.project.services;

import com.fifth_semester.project.entities.Fee;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.repositories.FeeRepository;
import com.fifth_semester.project.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Fee> getFeesForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return feeRepository.findByStudent(student);
    }

    // Get specific fee for a student by period
    public Fee getFeeByStudentAndPeriod(Long studentId, String period) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return feeRepository.findByStudentAndPeriod(student, period)
                .orElseThrow(() -> new RuntimeException("Fee statement not found for the specified period."));
    }

    // Make a fee payment (transactional to ensure consistency)
    @Transactional
    public String makeFeePayment(Long feeId, Double amount) {
        Fee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new RuntimeException("Fee record not found"));

        if (fee.getDueAmount() <= 0) {
            return "Fee already paid in full.";
        }

        double updatedPaidAmount = fee.getPaidAmount() + amount;
        double updatedDueAmount = fee.getTotalAmount() - updatedPaidAmount;

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
    @Transactional
    public Fee createOrUpdateFee(Long studentId, Double totalAmount, String period) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Optional<Fee> existingFee = feeRepository.findByStudentAndPeriod(student, period);

        Fee fee;
        if (existingFee.isPresent()) {
            fee = existingFee.get();
            fee.setTotalAmount(totalAmount);
            fee.setDueAmount(totalAmount - fee.getPaidAmount());
        } else {
            fee = new Fee(student, totalAmount, 0.0, totalAmount, "Unpaid", period);
            student.getFees().add(fee);
        }

        studentRepository.save(student);
        feeRepository.save(fee);
        return fee;
    }

    // Get all fee records for admin reporting
    public List<Fee> getAllFees() {
        return feeRepository.findAll();
    }

    // Get all unpaid fees for a student
    public List<Fee> getUnpaidFeesForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return feeRepository.findByStudentAndStatusIn(student, List.of("Unpaid", "Partially Paid"));
    }

    // Get the latest unpaid fee for a student
    public Fee getLatestUnpaidFeeForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return feeRepository.findTopByStudentAndStatusInOrderByPeriodDesc(student, List.of("Unpaid", "Partially Paid"))
                .orElseThrow(() -> new RuntimeException("No unpaid fee found for the student"));
    }

}
