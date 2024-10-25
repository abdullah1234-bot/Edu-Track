package com.fifth_semester.project.services;

import com.fifth_semester.project.dtos.response.GradeDTO;
import com.fifth_semester.project.entities.*;
import com.fifth_semester.project.entities.Section;
import com.fifth_semester.project.exception.BulkUploadException;
import com.fifth_semester.project.repositories.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import org.apache.commons.compress.archivers.dump.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

import com.fifth_semester.project.dtos.response.BulkUploadResponse;
@Service
public class ExamResultService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ExamRepository examRepository;

    private static final Logger logger = LoggerFactory.getLogger(ExamResultService.class);
    // Method for teachers to input or update a grade for a student
//    @Transactional
//    public String addOrUpdateGrade(String studentID, String courseName, String sectionName, int marks, String feedback) {
//
//        Student student = studentRepository.findByStudentId(studentID)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        Course course = courseRepository.findByCourseName(courseName)
//                .orElseThrow(() -> new RuntimeException("Course not found"));
//
//        Section section = sectionRepository.findBySectionNameAndCourse(sectionName,course)
//                .orElseThrow(() -> new RuntimeException("Section not found"));
//
//        Enrollment enrollment = enrollmentRepository.findByStudentAndCourseAndSection(student,course,section)
//                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
//
//        // Check if the grade already exists for the student in the course
//        Grade grade = gradeRepository.findByEnrollmentAndEnrollmentCourseAndEnrollmentSection(enrollment, course, section)
//                .orElse(new Grade(enrollment, marks, feedback));
//
//        // Set the updated marks and feedback
//        grade.setMarks(marks);
//        grade.setFeedback(feedback);
//
//        // Save or update the grade
//        gradeRepository.save(grade);
//        return "Grade saved successfully!";
//    }
    @Transactional
    public String addGrade(String studentID, Long courseId, String sectionName, int marks, String feedback,ExamType examType) {
        logger.debug("Processing grade assignment for studentEmail: {}, courseId: {}, sectionName: {}",
                studentID,courseId, sectionName);

        Student student = studentRepository.findByStudentId(studentID).orElseThrow(()->new RuntimeException("Student not found"));
        // Fetch Enrollment based on studentEmail, courseName, and sectionName
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByStudentStudentIdAndCourseIdAndSectionSectionName(
                studentID,
                courseId,
                sectionName
        );

        if (enrollmentOpt.isEmpty()) {
            String errorMsg = String.format("Enrollment not found for student email: %s, course name: %s, section name: %s",
                    studentID,
                    courseId,
                    sectionName);
            logger.error(errorMsg);
            return errorMsg;
        }

        Enrollment enrollment = enrollmentOpt.get();
        Course course = enrollment.getCourse();
        Section section = enrollment.getSection();

        // Fetch Exam based on courseId and examType
        Optional<Exam> examOpt = examRepository.findByCourseIdAndExamType(course.getId(), examType);
        if(examOpt.isEmpty()) {
            String errorMsg = String.format("No %s found for course: %s",
                    examType,
                    course.getCourseId()
                    );
            logger.error(errorMsg);
            return errorMsg;
        }
        Optional<Grade> gradeOpt = gradeRepository.findByEnrollmentAndEnrollmentCourseAndEnrollmentSectionAndExamType(enrollment, course, section, examType);
        Grade grade;
        if (gradeOpt.isPresent()) {
            return "Grade already uploaded!";
        }
        else {
            // Create new grade
            grade = new Grade();
            grade.setEnrollment(enrollment);
            grade.setMarks(marks);
            grade.setFeedback(feedback);
            grade.setExamType(examType);
            validateMarks(grade,student,course);
            logger.debug("Creating new grade for studentId: {}", studentID);
        }
        gradeRepository.save(grade);


        logger.info("Grade saved successfully for studentId: {}", studentID);

        return "Grade saved successfully!";
    }

    public String updateGrades(Long GradeId,int marks,String feedback) {
        Optional<Grade> gradeOpt = gradeRepository.findById(GradeId);
        Grade grade;
        if (gradeOpt.isPresent()) {
            grade=gradeOpt.get();
            Enrollment enrollment = grade.getEnrollment();
            Student student = enrollment.getStudent();
            Course course = enrollment.getCourse();
            // Update existing grade
            grade = gradeOpt.get();
            grade.setMarks(marks);
            grade.setFeedback(feedback);
            validateMarks(grade,student,course);
            gradeRepository.save(grade);
            logger.debug("Updating grade for studentEmail: {}", student.getEmail());
            return "Grade updated successfully!";
        }
        return "Grade Id Not Found!";
    }
    @Transactional
    public BulkUploadResponse bulkUploadGrades(Long courseId, String sectionName,MultipartFile file) throws IOException, InvalidFormatException, BulkUploadException {
        logger.debug("Starting bulk grade upload for courseId: {}, sectionName: {}", courseId, sectionName);
        List<String> errorDetails = new ArrayList<>();
            int totalRecords = 0;
            int successfulRecords = 0;
            int failedRecords = 0;

            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

            Iterator<Row> rows = sheet.iterator();
            if (!rows.hasNext()) {
                throw new BulkUploadException("The Excel file is empty.");
            }

            // Assuming the first row is the header
            Row headerRow = rows.next();
            Map<String, Integer> headerMap = getHeaderMap(headerRow);

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                totalRecords++;

                try {
                    String studentID = getCellValueAsString(currentRow.getCell(headerMap.get("studentID")));
                    String examTypeStr = getCellValueAsString(currentRow.getCell(headerMap.get("examType")));
                    int marks = (int) getCellValueAsNumeric(currentRow.getCell(headerMap.get("marks")));
                    String feedback = getCellValueAsString(currentRow.getCell(headerMap.get("feedback")));

                    ExamType examType;
                    try {
                        examType = ExamType.valueOf(examTypeStr.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new BulkUploadException("Invalid ExamType: " + examTypeStr + " at row " + (currentRow.getRowNum() + 1));
                    }

                    // Reuse the existing addOrUpdateGrade method
                    String result = addGrade(studentID, courseId, sectionName, marks, feedback, examType);
                    if (result.equals("Grade saved successfully!")) {
                        successfulRecords++;
                    } else {
                        failedRecords++;
                        errorDetails.add("Row " + (currentRow.getRowNum() + 1) + ": " + result);
                    }

                } catch (BulkUploadException e) {
                    failedRecords++;
                    errorDetails.add(e.getMessage());
                } catch (Exception e) {
                    failedRecords++;
                    errorDetails.add("Unexpected error at row " + (currentRow.getRowNum() + 1) + ": " + e.getMessage());
                }
            }

            workbook.close();

            String message = String.format("Bulk upload completed. Total: %d, Successful: %d, Failed: %d.", totalRecords, successfulRecords, failedRecords);
            return new BulkUploadResponse(message, totalRecords, successfulRecords, failedRecords, errorDetails);
        }

        private Map<String, Integer> getHeaderMap(Row headerRow) throws BulkUploadException {
            Map<String, Integer> headerMap = new HashMap<>();

            for (Cell cell : headerRow) {
                String headerName = cell.getStringCellValue().trim();
                headerMap.put(headerName, cell.getColumnIndex());
            }

            // Required headers
            List<String> requiredHeaders = Arrays.asList("studentID", "examType", "marks", "feedback");

            for (String header : requiredHeaders) {
                if (!headerMap.containsKey(header)) {
                    throw new BulkUploadException("Missing required header: " + header);
                }
            }

            return headerMap;
        }

        private String getCellValueAsString(Cell cell) {
            if (cell == null) return "";
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue().trim();
        }

        private Long getCellValueAsLong(Cell cell) {
            if (cell == null) return null;
            return (long) cell.getNumericCellValue();
        }

        private double getCellValueAsNumeric(Cell cell) {
            if (cell == null) return 0;
            return cell.getNumericCellValue();
        }

        // Method to get section-wise grades for a specific course
    public List<GradeDTO> getGradesForCourseAndSection(Long courseId, String sectionName) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Section section = sectionRepository.findBySectionNameAndCourse(sectionName,course)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        return gradeRepository.findByEnrollmentCourseAndEnrollmentSection(course, section);
    }

    // Method for students to view their grades for a specific course
    public List<GradeDTO> getGradesForStudent(Student student) {
        return gradeRepository.findByEnrollmentOfStudent(student);
    }

    // Get grades for a specific course
    public List<GradeDTO> getGradesForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return gradeRepository.findByEnrollmentCourse(course);
    }

    public String generateStudentTranscript(Student student) throws IOException, DocumentException {
        // Define the file path for the transcript
        String transcriptDir = "transcripts"; // Directory to store transcripts
        String transcriptPath = transcriptDir + "/transcript_" + student.getStudentId() + ".pdf";

        // Create directory if it doesn't exist
        java.nio.file.Path path = java.nio.file.Paths.get(transcriptDir);
        if (!java.nio.file.Files.exists(path)) {
            java.nio.file.Files.createDirectories(path);
        }

        // Initialize iText Document
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(transcriptPath));
        document.open();

        // Add Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
        Paragraph title = new Paragraph("Official Transcript", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add Student Information
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Paragraph studentInfo = new Paragraph(
                "Name: " + student.getUsername() + "\n" +
                        "Student ID: " + student.getStudentId() + "\n" +
                        "Email: " + student.getEmail() + "\n\n",
                infoFont
        );
        document.add(studentInfo);

        // Add Grades Table
        PdfPTable table = new PdfPTable(5); // 5 columns: Course ID, Course Name, Exam Type, Marks, Grade Value
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set Column Widths
        float[] columnWidths = {1f, 3f, 2f, 1f, 1f};
        table.setWidths(columnWidths);

        // Add Table Headers
        addTableHeader(table, "Course ID");
        addTableHeader(table, "Course Name");
        addTableHeader(table, "Exam Type");
        addTableHeader(table, "Marks");
        addTableHeader(table, "Grade");

        // Fetch and add grade data
        List<GradeDTO> grades = getGradesForStudent(student);
        for (GradeDTO grade : grades) {
            table.addCell(String.valueOf(grade.getCourseId()));
            table.addCell(grade.getCourseName());
            table.addCell(grade.getExamType().toString());
            table.addCell(String.valueOf(grade.getMarks()));
            table.addCell(grade.getGrade().toString());
        }

        document.add(table);

        // Calculate and add GPA or any other summary if needed

        // Close the document
        document.close();

        return transcriptPath;
    }
    private void addTableHeader(PdfPTable table, String headerTitle) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.DARK_GRAY);
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setPhrase(new Phrase(headerTitle, headerFont));
        table.addCell(header);
    }

    // Validation method
    private void validateMarks(Grade grade, Student student, Course course) {
        ExamType examType = grade.getExamType();
        int marks = grade.getMarks();

        switch (examType) {
            case MIDTERM_I:
            case MIDTERM_II:
                if (marks > 15) {
                    throw new RuntimeException("Marks for " + examType + " cannot exceed 15");
                }
                break;
            case FINAL:
                if (marks > 50) {
                    throw new RuntimeException("Marks for FINAL cannot exceed 50");
                }
                break;
            case QUIZ:
                Integer totalQuizMarks = gradeRepository.findTotalQuizMarks(student, course);
                if (totalQuizMarks == null) {
                    totalQuizMarks = 0; // If null, set to 0
                }
                if (totalQuizMarks + marks > 20) {
                    throw new RuntimeException("Total QUIZ marks cannot exceed 20 for this course");
                }
                break;
            default:
                throw new RuntimeException("Invalid Exam Type");
        }
    }

    // Helper method to convert Grade to GradeDTO
    private GradeDTO convertToDTO(Grade grade) {
        GradeDTO dto = new GradeDTO();
        dto.setId(grade.getId());
        dto.setStudentId(grade.getEnrollment().getStudent().getId().toString());
        dto.setStudentName(grade.getEnrollment().getStudent().getUsername());
        dto.setCourseId(grade.getEnrollment().getCourse().getId());
        dto.setCourseName(grade.getEnrollment().getCourse().getCourseName());
        dto.setSectionName(grade.getEnrollment().getSection().getSectionName());
        dto.setExamType(grade.getExamType());
        dto.setMarks(grade.getMarks());
        dto.setFeedback(grade.getFeedback());
        return dto;
    }

    // Private method to calculate GradeValue based on marks
    private GradeValue calculateGradeValue(int marks) {
        if (marks >= 85) {
            return GradeValue.A;
        } else if (marks >= 70) {
            return GradeValue.B;
        } else if (marks >= 60) {
            return GradeValue.C;
        } else if (marks >= 50) {
            return GradeValue.D;
        } else if (marks >= 40) {
            return GradeValue.E;
        } else {
            return GradeValue.F;  // F for marks less than 40
        }
    }

//    @Transactional
//    public String bulkUploadGrades(MultipartFile file, Long courseId) throws Exception {
//        // Fetch course by ID
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new RuntimeException("Course not found"));
//
//        // List to store uploaded grades
//        List<Grade> grades = new ArrayList<>();
//
//        // Read the Excel file
//        try (InputStream inputStream = file.getInputStream();
//             Workbook workbook = new XSSFWorkbook(inputStream)) {
//
//            Sheet sheet = workbook.getSheetAt(0);  // Assuming first sheet
//            DataFormatter formatter = new DataFormatter();
//
//            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {  // Skipping header row
//                Row row = sheet.getRow(i);
//                if (row == null) continue;
//
//                // Extract student ID and marks from the Excel row
//                String studentId = formatter.formatCellValue(row.getCell(0));  // Assuming student ID in 1st column
//                int marks = (int) row.getCell(1).getNumericCellValue();  // Assuming marks in 2nd column
//                String feedback = formatter.formatCellValue(row.getCell(2)); // Assuming feedback in 3rd column (optional)
//
//                // Fetch student by student ID
//                Student student = studentRepository.findByStudentId(studentId)
//                        .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));
//
//                // Create or update grade entry
//                Grade grade = new Grade(student, course, marks, feedback);
//                grades.add(grade);
//            }
//        }
//
//        // Save all grades to the repository
//        gradeRepository.saveAll(grades);
//        return "Bulk grade upload successful!";
//    }
}
