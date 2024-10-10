package com.fifth_semester.project.services;

import com.fifth_semester.project.dtos.response.TimetableRow;
import com.fifth_semester.project.entities.ClassSchedule;
import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Section;
import com.fifth_semester.project.entities.Teacher;
import com.fifth_semester.project.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TimetableService {

    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Transactional
    public String uploadTimetable(List<TimetableRow> timetableRows) {
        for (TimetableRow row : timetableRows) {
            String courseCode = row.getCourseCode();
            String sectionName = row.getSection();
            String instructorName = row.getInstructor();
            String venue = row.getVenue();
            String timeSlot = row.getTimeSlot();

            // Parse the section information
            String[] sectionParts = sectionName.split("-");
            String fieldOfStudy = sectionParts[0];  // e.g., BCS
            String semesterInfo = sectionParts[1];  // e.g., 5J
            int semesterNumber = Integer.parseInt(semesterInfo.substring(0, 1));  // 5
            String sectionLetter = semesterInfo.substring(1);  // J

            // Check if the course exists, if not, create it
            Optional<Course> courseOpt = courseRepository.findByCourseCodeAndSemester(courseCode, semesterNumber);
            Course course;
            if (courseOpt.isPresent()) {
                course = courseOpt.get();
            } else {
                course = new Course();
                course.setCourseCode(courseCode);
                course.setEligibleSemester(semesterNumber);
                course.setFieldOfStudy(fieldOfStudy);
                courseRepository.save(course);
            }

            // Check if the section exists, if not, create it
            Optional<Section> sectionOpt = sectionRepository.findByCourseAndSectionName(course, sectionLetter);
            Section section;
            if (sectionOpt.isPresent()) {
                section = sectionOpt.get();
            } else {
                section = new Section(sectionLetter);
                section.setCourse(course);
                sectionRepository.save(section);
            }

            // Check if the teacher exists by name
            Optional<Teacher> teacherOpt = teacherRepository.findByUsername(instructorName);
            Teacher teacher;
            if (teacherOpt.isPresent()) {
                teacher = teacherOpt.get();
            } else {
                throw new RuntimeException("Teacher not found: " + instructorName);
            }

            // Create and save the class schedule
            ClassSchedule schedule = new ClassSchedule(
                    course,
                    section,
                    teacher,
                    row.getClassDate(),
                    row.getStartTime(),
                    row.getEndTime(),
                    venue,
                    semesterNumber
            );
            classScheduleRepository.save(schedule);
        }

        return "Timetable uploaded and schedules created successfully.";
    }
}
