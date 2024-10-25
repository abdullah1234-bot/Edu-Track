package com.fifth_semester.project.services;

import com.fifth_semester.project.dtos.request.ClassScheduleReq;
import com.fifth_semester.project.dtos.response.ClassScheduleDTO;
import com.fifth_semester.project.dtos.response.TeacherClassScheduleDTO;
import com.fifth_semester.project.entities.ClassSchedule;
import com.fifth_semester.project.entities.Enrollment;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.entities.Teacher;
import com.fifth_semester.project.exception.ResourceNotFoundException;
import com.fifth_semester.project.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.upperCase;
import com.fifth_semester.project.entities.Section;
import com.fifth_semester.project.entities.Course;
@Service
public class ClassScheduleService {

    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private CourseRepository courseRepository;
    private static final Logger logger = LoggerFactory.getLogger(ClassScheduleService.class);

    // Retrieve class schedules for a student based on enrolled courses and date range
//    public List<ClassSchedule> getSchedulesForStudent(Student student, String day) {
//        day = day.toUpperCase();
//
//        // Get the student's enrollments for the current semester
//        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
//
//        // Log the number of enrollments
//        System.out.println("Enrollments size: " + enrollments.size());
//
//        // Extract course IDs and section IDs from enrollments
//        List<Long> courseIds = enrollments.stream()
//                .map(Enrollment::getSection)
//                .filter(Objects::nonNull)
//                .map(Section::getCourse)
//                .filter(Objects::nonNull)
//                .map(Course::getId)
//                .collect(Collectors.toList());
//
//        List<Long> sectionIds = enrollments.stream()
//                .map(Enrollment::getSection)
//                .filter(Objects::nonNull)
//                .map(Section::getId)
//                .collect(Collectors.toList());
//
//        System.out.println("Course IDs: " + courseIds);
//        System.out.println("Section IDs: " + sectionIds);
//
//        if (courseIds.isEmpty() && sectionIds.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        // Use Option B: Fetch schedules separately and combine
//        List<ClassSchedule> schedules = new ArrayList<>();
//
//        if (!courseIds.isEmpty()) {
//            schedules.addAll(classScheduleRepository.findByDayAndCourseIdIn(day, courseIds));
//        }
//
//        if (!sectionIds.isEmpty()) {
//            schedules.addAll(classScheduleRepository.findByDayAndSectionIdIn(day, sectionIds));
//        }
//
//        // Remove duplicates if necessary
//        schedules = schedules.stream().distinct().collect(Collectors.toList());
//
//        return schedules;
//    }
//    public List<ClassScheduleDTO> getSchedulesForStudent(Student student, String day) {
//        if (student == null) {
//            throw new IllegalArgumentException("Student cannot be null");
//        }
//
//        day = day.toUpperCase();
//
//        // Fetch enrollments for the student and current semester
//        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
//
//        // Log the number of enrollments
//        System.out.println("Enrollments size: " + enrollments.size());
//
//        // Extract course IDs and section IDs from enrollments
//        List<Long> courseIds = enrollments.stream()
//                .map(Enrollment::getSection)
//                .filter(Objects::nonNull)
//                .map(Section::getCourse)
//                .filter(Objects::nonNull)
//                .map(Course::getId)
//                .collect(Collectors.toList());
//
//        List<Long> sectionIds = enrollments.stream()
//                .map(Enrollment::getSection)
//                .filter(Objects::nonNull)
//                .map(Section::getId)
//                .collect(Collectors.toList());
//
//        System.out.println("Course IDs: " + courseIds);
//        System.out.println("Section IDs: " + sectionIds);
//
//        if (courseIds.isEmpty() && sectionIds.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        // Fetch schedules by course IDs
//        List<ClassSchedule> schedulesByCourse = new ArrayList<>();
//        if (!courseIds.isEmpty()) {
//            schedulesByCourse = classScheduleRepository.findByDayAndCourseIdIn(day, courseIds);
//        }
//
//        // Fetch schedules by section IDs
//        List<ClassSchedule> schedulesBySection = new ArrayList<>();
//        if (!sectionIds.isEmpty()) {
//            schedulesBySection = classScheduleRepository.findByDayAndSectionIdIn(day, sectionIds);
//        }
//
//        // Combine and remove duplicates
//        Set<ClassSchedule> combinedSchedules = new HashSet<>();
//        combinedSchedules.addAll(schedulesByCourse);
//        combinedSchedules.addAll(schedulesBySection);
//
//        // Map to DTOs
//        List<ClassScheduleDTO> scheduleDTOs = combinedSchedules.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//
//        return scheduleDTOs;
//    }
    public List<ClassScheduleDTO> getSchedulesForStudent(Student student, String day) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        day = day.toUpperCase();

        // Log student details
        logger.debug("Fetching schedules for Student ID: {}", student.getId());
        logger.debug("Student Semester: {}", student.getSemester());

        // Fetch enrollments for the student and current semester
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);

        // Log the number of enrollments
        logger.debug("Enrollments size: {}", enrollments.size());

        if (enrollments.isEmpty()) {
            logger.info("No enrollments found for Student ID: {} in Semester: {}", student.getId(), student.getSemester());
            return Collections.emptyList();
        }

        // Initialize a Set to store unique ClassSchedule objects
        Set<ClassSchedule> combinedSchedules = new HashSet<>();

        // Iterate over each enrollment to fetch class schedules
        for (Enrollment enrollment : enrollments) {
            Section section = enrollment.getSection();
            if (section == null) {
                logger.warn("Enrollment ID: {} has no associated section.", enrollment.getId());
                continue;
            }

            Long courseId = section.getCourse() != null ? section.getCourse().getId() : null;
            Long sectionId = section.getId();

            if (courseId == null) {
                logger.warn("Section ID: {} has no associated course.", sectionId);
                continue;
            }

            logger.debug("Processing Enrollment ID: {} - Course ID: {}, Section ID: {}", enrollment.getId(), courseId, sectionId);

            // Fetch class schedules for the specific day, course ID, and section ID
            List<ClassSchedule> schedules = classScheduleRepository.findByDayAndCourseIdAndSectionId(day, courseId, sectionId);

            logger.debug("Fetched {} class schedules for Course ID: {}, Section ID: {} on {}", schedules.size(), courseId, sectionId, day);

            // Add fetched schedules to the set to avoid duplicates
            combinedSchedules.addAll(schedules);
        }

        // Map the combined schedules to DTOs
        List<ClassScheduleDTO> scheduleDTOs = combinedSchedules.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        logger.debug("Mapped DTOs count: {}", scheduleDTOs.size());

        return scheduleDTOs;
    }


    private ClassScheduleDTO convertToDTO(ClassSchedule schedule) {
        if (schedule == null) {
            return null;
        }

        ClassScheduleDTO dto = new ClassScheduleDTO();
        dto.setId(schedule.getId());

        // Course details
        Course course = schedule.getCourse();
        if (course != null) {
            dto.setCourseId(course.getId());
            dto.setCourseName(course.getCourseName());
            dto.setCourseCode(course.getCourseCode());
        }

        // Section details
        Section section = schedule.getSection();
        if (section != null) {
            dto.setSectionName(section.getSectionName());
        }

        // Teacher details
        if (schedule.getTeacher() != null) {
            dto.setTeacherId(schedule.getTeacher().getId());
            dto.setTeacherName(schedule.getTeacher().getUsername());
        }

        // Schedule details
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());
        dto.setClassroom(schedule.getClassroom());
        dto.setDay(schedule.getDay());

        return dto;
    }

    // Retrieve class schedules for a teacher based on their assigned courses
    public List<TeacherClassScheduleDTO> getSchedulesForTeacher(Teacher teacher) {
        List<ClassSchedule> schedules = classScheduleRepository.findByTeacherId(teacher.getId());
        return schedules.stream()
                .map(this::convertToTeacherClassScheduleDTO)
                .collect(Collectors.toList());
    }
    private TeacherClassScheduleDTO convertToTeacherClassScheduleDTO(ClassSchedule schedule) {
        TeacherClassScheduleDTO dto = new TeacherClassScheduleDTO();
        dto.setId(schedule.getId());
        dto.setClassroom(schedule.getClassroom());
        dto.setCourseName(schedule.getCourse().getCourseName()); // Assuming a Course entity with getCourseName()
        dto.setDay(schedule.getDay());
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());
        dto.setSectionName(schedule.getSection().getSectionName()); // Assuming a Section entity with getSectionName()

        return dto;
    }
    // Create or update a class schedule (for teachers/admins)
    public void saveClassSchedule(ClassScheduleReq classScheduleReq) {

        Section section = sectionRepository.findBySectionNameAndCourseNameAndTeacherUsername(
                classScheduleReq.getSectionName(),
                classScheduleReq.getCourseName(),
                classScheduleReq.getTeacherName()
        ).orElseThrow(() -> new ResourceNotFoundException(
                "Section Not Found with sectionName: " + classScheduleReq.getSectionName() +
                        ", teacherName: " + classScheduleReq.getTeacherName() +
                        " and courseName: " + classScheduleReq.getCourseName()
        ));
        Teacher teacher = section.getTeacher();
            Course course = section.getCourse();
            ClassSchedule schedule = classScheduleRepository.findByDayAndCourseIdAndSectionIdAndStartTimeAndEndTime(classScheduleReq.getDay().toUpperCase(),course.getId(),section.getId(),classScheduleReq.getStartTime(),classScheduleReq.getEndTime())
                            .orElseGet(()-> {
                                ClassSchedule newSchedule = new ClassSchedule();
                                newSchedule.setInstructor(teacher);
                                newSchedule.setRoom(classScheduleReq.getClassroom());
                                newSchedule.setCourse(course);
                                newSchedule.setDay(classScheduleReq.getDay());
                                newSchedule.setStartTime(classScheduleReq.getStartTime());
                                newSchedule.setEndTime(classScheduleReq.getEndTime());
                                newSchedule.setSection(section);
                                newSchedule.setSemester(classScheduleReq.getSemester());
                                return newSchedule;
                            });
            classScheduleRepository.save(schedule);
    }

    // Retrieve all schedules for a specific course
    public List<ClassScheduleDTO> getSchedulesForCourse(Long courseId) {
        List<ClassSchedule> schedule = classScheduleRepository.findByCourseId(courseId);
        return schedule.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteclassschedule(ClassScheduleReq req){
        classScheduleRepository.findByDayAndCourseCourseNameAndSectionSectionNameAndStartTimeAndEndTime(
                req.getDay(),
                req.getCourseName(),
                req.getSectionName(),
                req.getStartTime(),
                req.getEndTime()
        ).orElseThrow(()->new RuntimeException("Schedule Not Found"));
    }

}
