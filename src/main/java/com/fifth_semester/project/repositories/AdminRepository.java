package com.fifth_semester.project.repositories;

import com.fifth_semester.project.dtos.response.InfoAdminDTO;
import com.fifth_semester.project.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);

    @Query("SELECT new com.fifth_semester.project.dtos.response.InfoAdminDTO(a.id, a.username, a.email, a.adminId) FROM Admin a WHERE a = :admin")
    InfoAdminDTO getAdminInfo(@Param("admin") Admin admin);
}
