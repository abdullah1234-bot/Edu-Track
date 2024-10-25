package com.fifth_semester.project.services;

import com.fifth_semester.project.dtos.response.StudentDTO;
import com.fifth_semester.project.entities.*;
import com.fifth_semester.project.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParentService {

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    // Get all children (students) for a parent
    public List<StudentDTO> getChildrenForParent(Parent parent) {
        return studentRepository.findStudentDTOsByParentId(parent.getId());
    }

    // Check if the student belongs to the parent
    private Student validateParentAccessToStudent(Parent parent, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        if (!parent.getChildren().contains(student)) {
            throw new RuntimeException("Unauthorized access to this student's data.");
        }
        return student;
    }

    // Get academic progress (grades) for a specific student
    public List<Grade> getAcademicProgressForStudent(Parent parent, Long studentId) {
        Student student = validateParentAccessToStudent(parent, studentId);
        return gradeRepository.findByEnrollmentStudent(student);
    }

    // Get attendance for a specific student
    public List<Attendance> getAttendanceForStudent(Parent parent, Long studentId) {
        Student student = validateParentAccessToStudent(parent, studentId);
        return attendanceRepository.findByStudent(student);
    }

//    // Get teacher feedback from both grades and assignments for a specific student
//    public List<Grade> getTeacherFeedbackFromGrades(Parent parent, Long studentId) {
//        Student student = validateParentAccessToStudent(parent, studentId);
//        return gradeRepository.findByEnrollmentStudent(student);
//    }

    public List<Assignment> getTeacherFeedbackFromAssignments(Parent parent, Long studentId) {
        Student student = validateParentAccessToStudent(parent, studentId);
        return assignmentRepository.findAssignmentsWithNonNullMarks(student);
    }
}
