package com.fifth_semester.project.controllers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fifth_semester.project.dtos.response.GradeDTO;
import com.fifth_semester.project.dtos.response.MessageResponse;
import com.fifth_semester.project.entities.ExamType;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.repositories.StudentRepository;
import com.fifth_semester.project.security.services.UserDetailsImpl;
import com.fifth_semester.project.services.ExamResultService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.fifth_semester.project.dtos.response.BulkUploadResponse;
import com.fifth_semester.project.exception.BulkUploadException;
@RestController
@RequestMapping("/api/exam_result")
@Tag(name = "Exam Result APIs")
public class ExamResultController {

    @Autowired
    private ExamResultService examResultService;

    @Autowired
    private StudentRepository studentRepository;

    private Student findStudent(String email)  throws IOException{
        return studentRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Student Not Found"));
    }
    // Endpoint for teachers to input or update a grade
    @PostMapping("/inputGrades")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> inputGrade(@RequestParam  String studentID,
                                        @RequestParam Long courseId,
                                        @RequestParam String sectionName,
                                        @RequestParam ExamType examType,
                                        @RequestParam int marks,
                                        @RequestParam(required = false) String feedback) {
        String result = examResultService.addGrade(studentID,courseId,sectionName,marks,feedback,examType);
        return ResponseEntity.ok(new MessageResponse(result));
    }

    @PutMapping("/updateGrades")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> updateGrade(@RequestParam  Long gradeId,@RequestParam int marks,
                                         @RequestParam(required = false) String feedback){
        String result = examResultService.updateGrades(gradeId,marks,feedback);
        return ResponseEntity.ok(new MessageResponse(result));
    }

    @PostMapping("/teacher/bulk-upload")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> bulkUploadGrades(@RequestParam("file") MultipartFile file,
                                              @RequestParam Long courseId,
                                              @RequestParam String sectionName) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Please upload a non-empty Excel file."));
        }

        try {
            BulkUploadResponse response = examResultService.bulkUploadGrades(courseId,sectionName,file);
            return ResponseEntity.ok(response);
        } catch (InvalidFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid Excel file format."));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error processing the Excel file."));
        } catch (BulkUploadException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
        }
    }

    // Endpoint to get section-wise grades for a specific course
    @GetMapping("/course/{courseId}/section/{sectionName}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> viewGradesForCourseAndSection(@PathVariable Long courseId, @PathVariable String sectionName) {
        List<GradeDTO> grades = examResultService.getGradesForCourseAndSection(courseId, sectionName);
        return ResponseEntity.ok(grades);
    }

    // Endpoint for students to view their grades
    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> viewGrades() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Student student;
        try {
             student = findStudent(userDetails.getEmail());

        }
        catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }
        List<GradeDTO> grades = examResultService.getGradesForStudent(student);
        return ResponseEntity.ok(grades);
    }

    // Endpoint to get grades for a specific course (admin or teacher)
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> viewGradesForCourse(@PathVariable Long courseId) {
        List<GradeDTO> grades = examResultService.getGradesForCourse(courseId);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/transcript")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Resource> downloadTranscript() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Student student = findStudent(userDetails.getEmail());
            String transcriptPath = examResultService.generateStudentTranscript(student);
            Path filePath = Paths.get(transcriptPath);

            Resource fileResource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName() + "\"")
                    .body(fileResource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


}
