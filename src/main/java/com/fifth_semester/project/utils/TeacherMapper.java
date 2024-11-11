package com.fifth_semester.project.utils;

import com.fifth_semester.project.dtos.response.CourseDTO;
import com.fifth_semester.project.dtos.response.TeacherDTO;
import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Teacher;

import java.util.List;
import java.util.stream.Collectors;

public class TeacherMapper {

    public static TeacherDTO mapToDTO(Teacher teacher) {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(teacher.getId());
        teacherDTO.setUsername(teacher.getUsername());
        teacherDTO.setEmail(teacher.getEmail());
        teacherDTO.setDepartment(teacher.getDepartment());
        teacherDTO.setOfficeHours(teacher.getOfficeHours());
        teacherDTO.setQualification(teacher.getQualification());
        teacherDTO.setSpecialization(teacher.getSpecialization());

        // Map the courses if needed
        List<CourseDTO> courses = teacher.getCourses().stream()
                .map(TeacherMapper::mapCourseToDTO)
                .collect(Collectors.toList());
        teacherDTO.setCourses(courses);

        return teacherDTO;
    }

    private static CourseDTO mapCourseToDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO(course.getId(),course.getCourseName(),course.getCourseCode(),course.getCreditHours(),course.getDescription());


        return courseDTO;
    }
}

