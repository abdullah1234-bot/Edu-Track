package com.fifth_semester.project.services;

import com.fifth_semester.project.dtos.request.CreateCourseDTO;
import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Enrollment;
import com.fifth_semester.project.entities.Section;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.repositories.CourseRepository;
import com.fifth_semester.project.repositories.EnrollmentRepository;
import com.fifth_semester.project.repositories.SectionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private SectionRepository sectionRepository;

    // Method to create a new course from DTO
    public Course createCourse(CreateCourseDTO courseDTO) {
        // Map the DTO to the Course entity
        Course course = new Course();
        course.setCourseName(courseDTO.getCourseName());
        course.setCourseCode(courseDTO.getCourseCode());
        course.setDescription(courseDTO.getDescription());
        course.setCreditHours(courseDTO.getCreditHours());
        course.setEligibleSemester(courseDTO.getEligibleSemester());
        course.setBacklogEligible(courseDTO.getBacklogEligible());
        course.setMaxCapacity(courseDTO.getMaxCapacity());
        course.setFieldOfStudy(courseDTO.getFieldOfStudy());
        course.setSemester(courseDTO.getSemester());

        // Save course to the repository
        return courseRepository.save(course);
    }

    // Fetch available courses for a student based on their academic standing
    public List<Course> getAvailableCoursesForStudent(Student student) {
        return courseRepository.findCoursesForStudent(student.getAcademicYear());
    }

    // Enroll a student in a course and assign them to a section
    @Transactional
    public String enrollStudentInCourse(Student student, Course course, boolean isBacklog) {
        int totalCreditHours = getTotalCreditHoursForStudent(student);

        if (totalCreditHours + course.getCreditHours() > 18) {
            return "Cannot enroll in course. Credit hour limit exceeded.";
        }

        if (isStudentAlreadyEnrolled(student, course)) {
            return "You are already enrolled in this course.";
        }

        if (course.getEnrollments().size() >= course.getMaxCapacity()) {
            return "Course capacity is full. Cannot enroll in this course.";
        }

        // Check for available sections or create a new section
        Section section = findOrCreateSectionForCourse(course);

        // Create an enrollment record and assign the student to the section
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setSemester(student.getAcademicYear());
        enrollment.setSection(section);
        enrollment.setCleared(false);

        // Save the enrollment
        enrollmentRepository.save(enrollment);

        return "Enrolled successfully in course and assigned to section " + section.getSectionName();
    }

//    private Section findOrCreateSectionForCourse(Course course) {
//        // Find the latest section for the course
//        Optional<Section> latestSection = sectionRepository.findFirstSectionByCourseWithStudentCountLessThan(course);
//        Section section;
//
//        if (latestSection.getPresentStatus() && latestSection.get().getStudentCount() < 50) {
//            section = latestSection.get();
//        } else {
//            int newSectionNumber = latestSection.getPresentStatus() ? latestSection.get().getSectionName().charAt(0) + 1 : 'A';
//            section = new Section(String.valueOf((char) newSectionNumber));
//            section.setCourse(course);
//            sectionRepository.save(section);
//        }
//        return section;
//    }
public Section findOrCreateSectionForCourse(Course course) {
    Optional<Section> optionalSection = sectionRepository.findFirstSectionByCourseWithStudentCountLessThan(course.getId());

    if (optionalSection.isPresent()) {
        // Section found with available capacity
        return optionalSection.get();
    } else {
        // No section found; create a new one
        return createNewSectionForCourse(course);
    }
}

    private Section createNewSectionForCourse(Course course) {
        Optional<Section> lastSectionOpt = sectionRepository.findTopByCourseOrderBySectionNameDesc(course);
        String newSectionName;

        if (lastSectionOpt.isPresent()) {
            String lastSectionName = lastSectionOpt.get().getSectionName();
            newSectionName = getNextSectionName(lastSectionName);
        } else {
            newSectionName = "A";
        }

        Section newSection = new Section(newSectionName,course);
        newSection.setCourse(course);
        sectionRepository.save(newSection);
        return newSection;
    }

    private String getNextSectionName(String lastSectionName) {
        // Implement logic to increment section names beyond 'Z' if necessary
        // For simplicity, we'll increment the last character
        char lastChar = lastSectionName.charAt(lastSectionName.length() - 1);
        if (lastChar == 'Z') {
            // Handle overflow, e.g., 'Z' to 'AA'
            return lastSectionName + 'A';
        } else {
            return lastSectionName.substring(0, lastSectionName.length() - 1) + (char) (lastChar + 1);
        }
    }

    private boolean isStudentAlreadyEnrolled(Student student, Course course) {
        return enrollmentRepository.existsByStudentAndCourse(student, course);
    }

    private int getTotalCreditHoursForStudent(Student student) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentAndSemester(student, student.getAcademicYear());
        return enrollments.stream().mapToInt(e -> e.getCourse().getCreditHours()).sum();
    }
}
