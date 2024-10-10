package com.fifth_semester.project.services;

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
    public List<Student> getChildrenForParent(Long parentId) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        return parent.getChildren();
    }

    // Check if the student belongs to the parent
    private void validateParentAccessToStudent(Long parentId, Long studentId) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!parent.getChildren().contains(student)) {
            throw new RuntimeException("Unauthorized access to this student's data.");
        }
    }

    // Get academic progress (grades) for a specific student
    public List<Grade> getAcademicProgressForStudent(Long parentId, Long studentId) {
        validateParentAccessToStudent(parentId, studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return gradeRepository.findByStudent(student);
    }

    // Get attendance for a specific student
    public List<Attendance> getAttendanceForStudent(Long parentId, Long studentId) {
        validateParentAccessToStudent(parentId, studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return attendanceRepository.findByStudent(student);
    }

    // Get teacher feedback from both grades and assignments for a specific student
    public List<Grade> getTeacherFeedbackFromGrades(Long parentId, Long studentId) {
        validateParentAccessToStudent(parentId, studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return gradeRepository.findByStudent(student);
    }

    public List<Assignment> getTeacherFeedbackFromAssignments(Long parentId, Long studentId) {
        validateParentAccessToStudent(parentId, studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return assignmentRepository.findByStudent(student);
    }
}
