package com.fifth_semester.project.controllers;

import com.fifth_semester.project.entities.Scholarship;
import com.fifth_semester.project.entities.ScholarshipStatus;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.repositories.StudentRepository;
import com.fifth_semester.project.security.services.UserDetailsImpl;
import com.fifth_semester.project.services.ScholarshipService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scholarships")
@Tag(name = "Scholarship APIs")
public class
ScholarshipController {

    @Autowired
    private ScholarshipService scholarshipService;
    @Autowired
    private StudentRepository studentRepository;


    // Endpoint for students to apply for a scholarship
    @PostMapping("/apply")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> applyForScholarship(@RequestParam String scholarshipName,
                                                           @RequestParam Double amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Student student = studentRepository.findByEmail(userDetails.getEmail()).orElseThrow(() -> new RuntimeException("Student not found"));
        String scholarship = scholarshipService.applyForScholarship(student, scholarshipName, amount);
        return ResponseEntity.ok(scholarship);
    }

    // Endpoint for admin to review and approve or reject a scholarship application
    @PostMapping("/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Scholarship> reviewScholarshipApplication(@RequestParam Long scholarshipId,
                                                                    @RequestParam ScholarshipStatus status) {
        Scholarship updatedScholarship = scholarshipService.reviewScholarshipApplication(scholarshipId, status);
        return ResponseEntity.ok(updatedScholarship);
    }

    // Endpoint to get all scholarships for a specific student
    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Scholarship>> getScholarshipsForStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Student student = studentRepository.findByEmail(userDetails.getEmail()).orElseThrow(() -> new RuntimeException("Student not found"));
        List<Scholarship> scholarships = scholarshipService.getScholarshipsForStudent(student);
        return ResponseEntity.ok(scholarships);
    }

    // Endpoint to get all pending scholarship applications (for admins)
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Scholarship>> getPendingScholarshipApplications() {
        List<Scholarship> pendingScholarships = scholarshipService.getPendingScholarshipApplications();
        return ResponseEntity.ok(pendingScholarships);
    }

    // Endpoint to get all approved scholarships (for admin reporting)
    @GetMapping("/approved")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Scholarship>> getAllApprovedScholarships() {
        List<Scholarship> approvedScholarships = scholarshipService.getAllApprovedScholarships();
        return ResponseEntity.ok(approvedScholarships);
    }
}
