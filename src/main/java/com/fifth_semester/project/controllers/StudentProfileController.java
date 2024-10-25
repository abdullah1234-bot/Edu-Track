package com.fifth_semester.project.controllers;

import com.fifth_semester.project.dtos.request.UpdateStudentProfileDTO;
import com.fifth_semester.project.dtos.request.passwordUpdate;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.repositories.StudentRepository;
import com.fifth_semester.project.security.services.UserDetailsImpl;
import com.fifth_semester.project.services.StudentProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import java.io.IOException;

@RestController
@RequestMapping("/api/student")
@Tag(name = "Student Profile Management APIs")
public class StudentProfileController {

    @Autowired
    private StudentProfileService studentProfileService;

    @Autowired
    private StudentRepository studentRepository;

    // Endpoint for updating the student's profile
    @PutMapping("/update/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> updateStudentProfile(
            @Valid @RequestBody UpdateStudentProfileDTO updateStudentProfileDTO
    ) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Student student = studentRepository.findByEmail(userDetails.getEmail())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            String result = studentProfileService.updateStudentProfile(student.getId(), updateStudentProfileDTO.getUsername(), updateStudentProfileDTO.getAddress(), updateStudentProfileDTO.getEmergencyContact());
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error uploading profile picture.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update/password")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> changeStudentPassword(
            @Valid @RequestBody passwordUpdate ps) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Student student = studentRepository.findByEmail(userDetails.getEmail())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            String result = studentProfileService.changeStudentPassword(student.getId(), ps.getCurrentPassword(), ps.getNewPassword());
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
