package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
//    List<Notification> findByRecipientId(Long recipientId);
}
