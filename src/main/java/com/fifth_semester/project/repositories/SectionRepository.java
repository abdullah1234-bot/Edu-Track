package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    // Define method signature without a method body
    Optional<Section> findByCourseAndSectionName(Course course, String sectionName);

    @Query("SELECT s FROM Section s WHERE s.course = :course ORDER BY s.sectionName DESC")
    Optional<Section> findLatestSectionByCourse(Course course);
}
