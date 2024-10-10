package com.fifth_semester.project.services;

import com.fifth_semester.project.dtos.response.TeacherPerformanceDTO;
import com.fifth_semester.project.entities.Teacher;
import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.repositories.TeacherRepository;
import com.fifth_semester.project.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Method to create a new teacher
    public String createTeacher(String username, String email, String department, String officeHours,
                                String qualification, String specialization, LocalDate dateOfHire) {
        // Create new teacher
        Teacher teacher = new Teacher();
        teacher.setUsername(username);
        teacher.setEmail(email);
        teacher.setDepartment(department);
        teacher.setOfficeHours(officeHours);
        teacher.setQualification(qualification);
        teacher.setSpecialization(specialization);
        teacher.setDateOfHire(dateOfHire);

        // Save the teacher to the repository
        teacherRepository.save(teacher);

        return "Teacher created successfully";
    }

    // Method to update an existing teacher
    public String updateTeacher(Long teacherId, String username, String email, String department, String officeHours,
                                String qualification, String specialization, LocalDate dateOfHire) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // Update the fields only if they are provided
        if (username != null) {
            teacher.setUsername(username);
        }
        if (email != null) {
            teacher.setEmail(email);
        }
        if (department != null) {
            teacher.setDepartment(department);
        }
        if (officeHours != null) {
            teacher.setOfficeHours(officeHours);
        }
        if (qualification != null) {
            teacher.setQualification(qualification);
        }
        if (specialization != null) {
            teacher.setSpecialization(specialization);
        }
        if (dateOfHire != null) {
            teacher.setDateOfHire(dateOfHire);
        }

        // Save the updated teacher to the repository
        teacherRepository.save(teacher);

        return "Teacher profile updated successfully";
    }

    public String changeTeacherPassword(Long teacherId, String previousPassword, String newPassword) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // Verify the previous password
        if (!passwordEncoder.matches(previousPassword, teacher.getPassword())) {
            throw new RuntimeException("Previous password is incorrect.");
        }

        // Encode the new password
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        // Update the teacher's password
        teacher.setPassword(encodedNewPassword);
        teacherRepository.save(teacher);

        return "Password changed successfully.";
    }

    // Assign teacher to a course
    public String assignTeacherToCourse(Long teacherId, Long courseId) {
        Optional<Teacher> teacherOpt = teacherRepository.findById(teacherId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (teacherOpt.isEmpty()) {
            return "Teacher not found";
        }

        if (courseOpt.isEmpty()) {
            return "Course not found";
        }

        Teacher teacher = teacherOpt.get();
        Course course = courseOpt.get();

        course.setTeacher(teacher);

        // Add course to the teacher's list of courses if it's not already added
        if (!teacher.getCourses().contains(course)) {
            teacher.getCourses().add(course);
        }

        teacherRepository.save(teacher);
        courseRepository.save(course);
        return "Teacher assigned to course successfully";
    }

    // Remove the assigned teacher from a course and update the teacher's course list
    public String removeTeacherFromCourse(Long teacherId, Long courseId) {
        Optional<Teacher> teacherOpt = teacherRepository.findById(teacherId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (teacherOpt.isEmpty()) {
            return "Teacher not found";
        }

        if (courseOpt.isEmpty()) {
            return "Course not found";
        }

        Teacher teacher = teacherOpt.get();
        Course course = courseOpt.get();

        // Check if the course is currently assigned to the teacher
        if (course.getTeacher() == null || !course.getTeacher().getId().equals(teacherId)) {
            return "The course is not assigned to this teacher";
        }

        // Remove the course from the teacher's list of courses
        teacher.getCourses().remove(course);

        // Set the course's teacher to null (unassign the teacher)
        course.setTeacher(null);

        // Save both the teacher and course entities
        teacherRepository.save(teacher);
        courseRepository.save(course);

        return "Teacher removed from course successfully";
    }


    // Get teacher by ID
    public Optional<Teacher> getTeacherById(Long teacherId) {
        return teacherRepository.findById(teacherId);
    }

    // Get all teachers
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    // Delete teacher by ID
    public void deleteTeacher(Long teacherId) {
        teacherRepository.deleteById(teacherId);
    }

    // Get courses assigned to a teacher
    public List<Course> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    // Track teacher's performance (e.g., by number of courses, student enrollment)
    public TeacherPerformanceDTO getTeacherPerformance(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        List<Course> courses = courseRepository.findByTeacherId(teacherId);
        int totalCourses = courses.size();
        int totalEnrollments = courses.stream().mapToInt(course -> course.getEnrollments().size()).sum();

        return new TeacherPerformanceDTO(teacher, totalCourses, totalEnrollments);
    }
}
