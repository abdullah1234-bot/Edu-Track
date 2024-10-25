package com.fifth_semester.project.services;

import com.fifth_semester.project.entities.Fee;
import com.fifth_semester.project.entities.Scholarship;
import com.fifth_semester.project.entities.ScholarshipStatus;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.repositories.FeeRepository;
import com.fifth_semester.project.repositories.ScholarshipRepository;
import com.fifth_semester.project.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ScholarshipService {

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FeeRepository feeRepository;

    // Student applies for a scholarship
    public String applyForScholarship(Student student, String scholarshipName, Double amount) {
        // Create a new scholarship application
        Scholarship scholarshipOpt = scholarshipRepository.findByNameAndStudent(scholarshipName,student);
        if(scholarshipOpt != null) {
            return "Scholarship application already exists";
        }
        Scholarship scholarship = new Scholarship(student, scholarshipName, amount, ScholarshipStatus.PENDING, LocalDate.now(), null);
        scholarshipRepository.save(scholarship);
        return "Scholarship Applied";
    }

    // Admin reviews and approves or rejects a scholarship application
    @Transactional
    public Scholarship reviewScholarshipApplication(Long scholarshipId, ScholarshipStatus status) {
        Scholarship scholarship = scholarshipRepository.findById(scholarshipId)
                .orElseThrow(() -> new RuntimeException("Scholarship application not found"));

        if (status == ScholarshipStatus.APPROVED) {
            // Apply the scholarship to unpaid fees if it's approved
            applyScholarshipToFees(scholarship);
        }

        // Update scholarship status and decision date
        scholarship.setStatus(status);
        scholarship.setDecisionDate(LocalDate.now());
        return scholarshipRepository.save(scholarship);
    }

    // Apply the scholarship amount to the latest unpaid fees for the student
    private void applyScholarshipToFees(Scholarship scholarship) {
        Student student = scholarship.getStudent();
        Optional<List<Fee>> unpaidFeesOpt = feeRepository.findUnpaidFeesByStudent(student.getId());

        double remainingScholarshipAmount = scholarship.getAmount();

        if (unpaidFeesOpt.isPresent()) {
            List<Fee> unpaidFees = unpaidFeesOpt.get();
            for (Fee fee : unpaidFees) {
                if (remainingScholarshipAmount <= 0) {
                    break;
                }

                double dueAmount = fee.getDueAmount();
                if (dueAmount > 0) {
                    // Calculate the deduction to apply from the scholarship
                    double deduction = Math.min(remainingScholarshipAmount, dueAmount);

                    // Update the fee's paid amount and due amount
                    fee.setPaidAmount(fee.getPaidAmount() + deduction);
                    fee.setDueAmount(fee.getDueAmount() - deduction);

                    // If the fee is fully paid, mark it as "Paid"
                    if (fee.getDueAmount() <= 0) {
                        fee.setStatus("Paid");
                    } else {
                        fee.setStatus("Partially Paid");
                    }

                    // Save the updated fee
                    feeRepository.save(fee);

                    // Subtract the deduction from the remaining scholarship amount
                    remainingScholarshipAmount -= deduction;
                }
            }
        }
    }

    // Get all scholarships for a student
    public List<Scholarship> getScholarshipsForStudent(Student student) {
        return scholarshipRepository.findByStudent(student);
    }

    // Get all pending scholarship applications (for admins)
    public List<Scholarship> getPendingScholarshipApplications() {
        return scholarshipRepository.findByStatus(ScholarshipStatus.PENDING);
    }

    // Get all approved scholarships (for reporting purposes)
    public List<Scholarship> getAllApprovedScholarships() {
        return scholarshipRepository.findByStatus(ScholarshipStatus.APPROVED);
    }
}
