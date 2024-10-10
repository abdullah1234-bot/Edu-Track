package com.fifth_semester.project.services;

import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Grade;
import com.fifth_semester.project.entities.Section;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.repositories.CourseRepository;
import com.fifth_semester.project.repositories.GradeRepository;
import com.fifth_semester.project.repositories.SectionRepository;
import com.fifth_semester.project.repositories.StudentRepository;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    // Method for teachers to input or update a grade for a student
    @Transactional
    public String addOrUpdateGrade(Long studentId, Long courseId, int marks, String feedback) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Check if the grade already exists for the student in the course
        Grade grade = gradeRepository.findByStudentAndCourse(student, course)
                .orElse(new Grade(student, course, marks, feedback));

        // Set the updated marks and feedback
        grade.setMarks(marks);
        grade.setFeedback(feedback);

        // Save or update the grade
        gradeRepository.save(grade);
        return "Grade saved successfully!";
    }

    // Method to get section-wise grades for a specific course
    public List<Grade> getGradesForCourseAndSection(Long courseId, Long sectionId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        return gradeRepository.findByCourseAndCourseSection(course, section);
    }

    // Method for students to view their grades for a specific course
    public List<Grade> getGradesForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return gradeRepository.findByStudent(student);
    }

    // Get grades for a specific course
    public List<Grade> getGradesForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return gradeRepository.findByCourse(course);
    }

    @Transactional
    public String bulkUploadGrades(MultipartFile file, Long courseId) throws Exception {
        // Fetch course by ID
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // List to store uploaded grades
        List<Grade> grades = new ArrayList<>();

        // Read the Excel file
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);  // Assuming first sheet
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {  // Skipping header row
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Extract student ID and marks from the Excel row
                String studentId = formatter.formatCellValue(row.getCell(0));  // Assuming student ID in 1st column
                int marks = (int) row.getCell(1).getNumericCellValue();  // Assuming marks in 2nd column
                String feedback = formatter.formatCellValue(row.getCell(2)); // Assuming feedback in 3rd column (optional)

                // Fetch student by student ID
                Student student = studentRepository.findByStudentId(studentId)
                        .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

                // Create or update grade entry
                Grade grade = new Grade(student, course, marks, feedback);
                grades.add(grade);
            }
        }

        // Save all grades to the repository
        gradeRepository.saveAll(grades);
        return "Bulk grade upload successful!";
    }
}
