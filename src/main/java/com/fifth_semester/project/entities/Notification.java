package com.fifth_semester.project.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime notificationDate;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;  // Enum for type (e.g., Assignment Deadline, Exam Schedule)

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;  // Enum for status (Read, Unread)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Notification is linked to a specific user

    public Notification() {}

    public Notification(String message, NotificationType notificationType, NotificationStatus status, User user, LocalDateTime notificationDate) {
        this.message = message;
        this.notificationType = notificationType;
        this.status = status;
        this.user = user;
        this.notificationDate = notificationDate;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(LocalDateTime notificationDate) {
        this.notificationDate = notificationDate;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
