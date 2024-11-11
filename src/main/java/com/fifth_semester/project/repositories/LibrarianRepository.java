package com.fifth_semester.project.repositories;

import com.fifth_semester.project.dtos.response.InfoLibrarianDTO;
import com.fifth_semester.project.entities.Librarian;
import com.fifth_semester.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long> {
    Optional<Librarian> findByEmail(String email);

    @Query("SELECT new com.fifth_semester.project.dtos.response.InfoLibrarianDTO(l.id, l.username, l.email, l.employeeId, l.librarySection) FROM Librarian l " +
            "WHERE l = :librarian")
    InfoLibrarianDTO getLibrarianInfo(@Param("librarian") Librarian librarian);
}
