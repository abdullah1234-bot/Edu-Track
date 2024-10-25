package com.fifth_semester.project.services;

import com.fifth_semester.project.dtos.response.TeacherDTO;
import com.fifth_semester.project.dtos.response.TeacherPerformanceDTO;
import com.fifth_semester.project.entities.Section;
import com.fifth_semester.project.entities.Teacher;
import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.repositories.SectionRepository;
import com.fifth_semester.project.repositories.TeacherRepository;
import com.fifth_semester.project.repositories.CourseRepository;
import com.fifth_semester.project.utils.TeacherMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);
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
//    public String assignTeacherToCourse(Long teacherId, Long courseId) {
//        Optional<Teacher> teacherOpt = teacherRepository.findById(teacherId);
//        Optional<Course> courseOpt = courseRepository.findById(courseId);
//
//        if (teacherOpt.isEmpty()) {
//            return "Teacher not found";
//        }
//
//        if (courseOpt.isEmpty()) {
//            return "Course not found";
//        }
//
//        Teacher teacher = teacherOpt.get();
//        Course course = courseOpt.get();
//
//        course.setTeacher(teacher);
//
//        // Add course to the teacher's list of courses if it's not already added
//        if (!teacher.getCourses().contains(course)) {
//            teacher.getCourses().add(course);
//        }
//
//        teacherRepository.save(teacher);
//        courseRepository.save(course);
//        return "Teacher assigned to course successfully";
//    }
    @Transactional
    public String assignTeacherToCourseAndSection(Long teacherId, Long courseId, Long sectionId) {
        logger.debug("Assigning teacher with email '{}' to course '{}' and section '{}'", teacherId, courseId, sectionId);

        // Fetch Teacher by email
        Optional<Teacher> teacherOpt = teacherRepository.findById(teacherId);
        if (teacherOpt.isEmpty()) {
            logger.warn("Teacher not found with email: {}", teacherId);
            return "Teacher not found with email: " + teacherId;
        }
        Teacher teacher = teacherOpt.get();

        // Fetch Course by name
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) {
            logger.warn("Course not found with name: {}", courseId);
            return "Course not found with name: " + courseId;
        }
        Course course = courseOpt.get();

        // Fetch Section by name and associated course
        Optional<Section> sectionOpt = sectionRepository.findByIdAndCourse(sectionId, course);
        if (sectionOpt.isEmpty()) {
            logger.warn("Section not found with name: {} for course: {}", sectionId, courseId);
            return "Section not found with name: " + sectionId + " for course: " + courseId;
        }
        Section section = sectionOpt.get();

        // Assign Teacher to Section if not already assigned
        if (section.getTeacher() == null || !section.getTeacher().equals(teacher)) {
            section.setTeacher(teacher);
            teacher.getSections().add(section);
            sectionRepository.save(section);
            teacherRepository.save(teacher);
            logger.info("Assigned teacher '{}' to course '{}' and section '{}'", teacher.getEmail(), course.getCourseName(), section.getSectionName());
        } else {
            logger.info("Teacher '{}' is already assigned to course '{}' and section '{}'", teacher.getEmail(), course.getCourseName(), section.getSectionName());
        }

        return "Teacher assigned to course and section successfully";
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
    public List<TeacherDTO> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();

        // Map List<Teacher> to List<TeacherDTO>
        return teachers.stream()
                .map(TeacherMapper::mapToDTO)
                .collect(Collectors.toList());
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
