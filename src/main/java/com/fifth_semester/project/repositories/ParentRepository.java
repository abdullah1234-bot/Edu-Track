package com.fifth_semester.project.repositories;

import com.fifth_semester.project.dtos.response.InfoParentDTO;
import com.fifth_semester.project.entities.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    Optional<Parent> findByEmail(String email);
    @Query("SELECT new com.fifth_semester.project.dtos.response.InfoParentDTO(" +
            "p.id, p.username, p.email, p.contactNumber, p.address, p.occupation) " +
            "FROM Parent p WHERE p.email = :email")
    InfoParentDTO getParentInfo(@Param("email") String email);


//    Optional<Parent> findByStudentId(Long id);
}
