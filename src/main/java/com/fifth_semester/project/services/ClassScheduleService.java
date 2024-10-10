package com.fifth_semester.project.services;

import com.fifth_semester.project.entities.ClassSchedule;
import com.fifth_semester.project.entities.Enrollment;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.entities.Teacher;
import com.fifth_semester.project.repositories.ClassScheduleRepository;
import com.fifth_semester.project.repositories.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassScheduleService {

    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // Retrieve class schedules for a student based on enrolled courses and date range
    public List<ClassSchedule> getSchedulesForStudent(Student student, LocalDate startDate, LocalDate endDate) {
        // Get the course IDs for the student's enrolled courses
        List<Long> courseIds = enrollmentRepository.findByStudentAndSemester(student, student.getAcademicYear())
                .stream()
                .map(Enrollment::getCourse)  // Get the courses the student is enrolled in
                .map(course -> course.getId())  // Extract course IDs
                .collect(Collectors.toList());

        // Retrieve the schedules for the student's courses within the date range
        return classScheduleRepository.findByCourseIdInAndClassDateBetween(courseIds, startDate, endDate);
    }


    // Retrieve class schedules for a teacher based on their assigned courses
    public List<ClassSchedule> getSchedulesForTeacher(Teacher teacher) {
        return classScheduleRepository.findByTeacherId(teacher.getId());
    }

    // Create or update a class schedule (for teachers/admins)
    public ClassSchedule saveClassSchedule(ClassSchedule classSchedule) {
        return classScheduleRepository.save(classSchedule);
    }

    // Retrieve all schedules for a specific course
    public List<ClassSchedule> getSchedulesForCourse(Long courseId) {
        return classScheduleRepository.findByCourseId(courseId);
    }
}
