package com.fifth_semester.project.services;

import com.fifth_semester.project.entities.*;
import com.fifth_semester.project.repositories.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExamResultService {

    @Autowired
    private ExamResultRepository examResultRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Using the /uploads/transcripts directory for better flexibility
    private final String transcriptStoragePath = "uploads/transcripts/";

    public ExamResultService() throws IOException {
        // Ensure that the transcript directory exists in /uploads/transcripts
        Path transcriptDir = Paths.get(transcriptStoragePath);
        if (!Files.exists(transcriptDir)) {
            Files.createDirectories(transcriptDir);
        }
    }

    // Method to get exam results for a student
    public List<ExamResult> getExamResultsForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return examResultRepository.findByStudent(student);
    }

//    // Fetch exam results for a specific student in a given semester
//    public List<ExamResult> getExamResultsForStudentBySemester(Long studentId, int semester) {
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        return examResultRepository.findExamResultsByStudentAndSemester(student, semester);
//    }

    // Fetch exam results of a specific exam type (e.g., MIDTERM, FINAL) for a specific course
    public List<ExamResult> getExamResultsByCourseAndExamType(Long studentId, Long courseId, ExamType examType) {
        // Fetch the student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Fetch the course (should use courseRepository instead of examRepository)
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Fetch exam results by student, course, and exam type
        return examResultRepository.findExamResultsByStudentAndCourseAndExamExamType(student, course, examType);
    }


    // Method to aggregate exam results and generate transcript view
    public List<Grade> getStudentTranscriptDetails(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Fetching all grades for the student across courses
        return gradeRepository.findByStudent(student);
    }

    // Method to generate and return the PDF transcript path
    public String generateStudentTranscript(Long studentId) throws IOException, DocumentException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Fetch grades and results for the student
        List<Grade> grades = gradeRepository.findByStudent(student);

        if (grades.isEmpty()) {
            throw new RuntimeException("No grades found for transcript generation.");
        }

        // Generate transcript PDF
        String transcriptPath = generateTranscriptPDF(student, grades);
        return transcriptPath;
    }

    // Private method to generate a PDF transcript
    private String generateTranscriptPDF(Student student, List<Grade> grades) throws IOException, DocumentException {
        // Updated to save the transcript under /uploads/transcripts
        String transcriptFileName = transcriptStoragePath + student.getStudentId() + "_transcript.pdf";
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(transcriptFileName));
            document.open();

            // Add student and transcript header
            document.add(new Paragraph("Transcript for: " + student.getUsername()));
            document.add(new Paragraph("Student ID: " + student.getStudentId()));
            document.add(new Paragraph("Academic Year: " + student.getAcademicYear()));
            document.add(new Paragraph("Date: " + LocalDate.now().toString()));
            document.add(new Paragraph("---------------------------------------------------"));

            // Add grades for each course
            for (Grade grade : grades) {
                document.add(new Paragraph(
                        "Course: " + grade.getCourse().getCourseName() +
                                " | Marks: " + grade.getMarks() +
                                " | Grade: " + grade.getValue()
                ));
            }

            document.add(new Paragraph("---------------------------------------------------"));
            document.add(new Paragraph("Generated on: " + LocalDate.now().toString()));
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
            throw e; // Re-throw exception after logging
        } finally {
            document.close(); // Close the document after adding content
        }

        return transcriptFileName; // Return the path to the PDF
    }
}
