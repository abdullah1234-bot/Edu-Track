package com.fifth_semester.project.controllers;

import com.fifth_semester.project.services.StudentProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/student")
@Tag(name = "Student Profile Management APIs")
public class StudentProfileController {

    @Autowired
    private StudentProfileService studentProfileService;

    // Endpoint for updating the student's profile
    @PutMapping("/update/profile/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> updateStudentProfile(
            @PathVariable Long studentId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String emergencyContact,
            @RequestParam(required = false) MultipartFile profilePicture) {

        try {
            String result = studentProfileService.updateStudentProfile(studentId, username, address, emergencyContact, profilePicture);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error uploading profile picture.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update/password/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> changeStudentPassword(
            @PathVariable Long studentId,
            @RequestParam String previousPassword,
            @RequestParam String newPassword) {

        try {
            String result = studentProfileService.changeStudentPassword(studentId, previousPassword, newPassword);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
