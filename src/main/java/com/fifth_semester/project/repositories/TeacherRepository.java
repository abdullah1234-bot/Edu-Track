package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByTeacherId(String teacherId);
    Optional<Teacher> findByEmail(String email);
    Optional<Teacher> findByUsername(String username);
    @Query("SELECT t FROM Teacher t WHERE t.username = :username OR t.email = :email")
    Optional<Teacher> findByUsernameOrEmail(String username, String email);
}