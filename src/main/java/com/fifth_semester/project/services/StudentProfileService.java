package com.fifth_semester.project.services;

import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StudentProfileService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String profilePictureStoragePath = "src/main/resources/static/profiles/";

    // Update the student's username, password, profile picture, emergency contact, and address
    public String updateStudentProfile(Long studentId, String username, String address, String emergencyContact) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Update username
        if (username != null && !username.isEmpty()) {
            student.setUsername(username);
        }

//        update address
        if (address != null && !address.isEmpty()) {
            student.setAddress(address);
        }
        // Update contact
        if (emergencyContact != null && !emergencyContact.isEmpty()) {
            student.setEmergencyContact(emergencyContact);
        }
        // Update profile picture
//        if (profilePicture != null && !profilePicture.isEmpty()) {
//            String profilePicturePath = saveProfilePicture(profilePicture);
//            student.setProfilePicture(profilePicturePath);
//        }

        studentRepository.save(student);
        return "Student profile updated successfully!";
    }

    // Method to change the student's password
    public String changeStudentPassword(Long studentId, String previousPassword, String newPassword) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Verify the previous password
        if (!passwordEncoder.matches(previousPassword, student.getPassword())) {
            throw new RuntimeException("Previous password is incorrect.");
        }

        // Encode the new password
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        // Update the student's password
        student.setPassword(encodedNewPassword);
        studentRepository.save(student);

        return "Password changed successfully.";
    }

    // Helper method to save profile picture
//    private String saveProfilePicture(MultipartFile profilePicture) throws IOException {
//        String fileName = profilePicture.getOriginalFilename();
//        Path filePath = Paths.get(profilePictureStoragePath, fileName);
//        Files.createDirectories(filePath.getParent());  // Ensure directories exist
//        Files.write(filePath, profilePicture.getBytes());
//        return "/profiles/" + fileName;  // Return relative path to be stored in the database
//    }
}
