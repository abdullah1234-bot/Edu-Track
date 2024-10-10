package com.fifth_semester.project.controllers;

import com.fifth_semester.project.entities.ExamResult;
import com.fifth_semester.project.entities.ExamType;
import com.fifth_semester.project.entities.Grade;
import com.fifth_semester.project.services.ExamResultService;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/exam-results")
@Tag(name = "Exam Result APIs")
public class ExamResultController {

    @Autowired
    private ExamResultService examResultService;

    // Endpoint to get all exam results for a student
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Grade>> getExamResultsForStudent(@PathVariable Long studentId) {
        List<Grade> examResults = examResultService.getStudentTranscriptDetails(studentId);
        return ResponseEntity.ok(examResults);
    }

//    // Get exam results for a student by semester
//    @GetMapping("/student/{studentId}/semester/{semester}")
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<List<ExamResult>> getExamResultsBySemester(@PathVariable Long studentId, @PathVariable int semester) {
//        List<ExamResult> examResults = examResultService.getExamResultsForStudentBySemester(studentId, semester);
//        return ResponseEntity.ok(examResults);
//    }

    // Get exam results by course and exam type for a student
    @GetMapping("/student/{studentId}/course/{courseId}/exam-type")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ExamResult>> getExamResultsByCourseAndExamType(@PathVariable Long studentId,
                                                                              @PathVariable Long courseId,
                                                                              @RequestParam ExamType examType) {
        List<ExamResult> examResults = examResultService.getExamResultsByCourseAndExamType(studentId, courseId, examType);
        return ResponseEntity.ok(examResults);
    }

    // Endpoint to generate and retrieve student transcript link (path to the PDF file)
    @GetMapping("/transcript/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> getStudentTranscriptLink(@PathVariable Long studentId) {
        try {
            String transcriptPath = examResultService.generateStudentTranscript(studentId);
            return ResponseEntity.ok("Transcript available at: " + transcriptPath);
        } catch (IOException | DocumentException e) {
            return ResponseEntity.status(500).body("Error generating transcript: " + e.getMessage());
        }
    }

    @GetMapping("/download/{studentId}")
    public ResponseEntity<Resource> downloadTranscript(@PathVariable Long studentId) throws IOException, DocumentException {
        String transcriptPath = examResultService.generateStudentTranscript(studentId);
        Path filePath = Paths.get(transcriptPath);

        Resource fileResource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName() + "\"")
                .body(fileResource);
    }

}
