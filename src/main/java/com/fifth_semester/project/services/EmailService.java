package com.fifth_semester.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    // Email sent to student after approval
    public void sendStudentApprovalEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Account Has Been Approved");
        message.setText("Dear " + username + ", \n\n" +
                "Your student account has been approved. You can now log in to the system.\n\n" +
                "Best regards,\nEduTrack");
        emailSender.send(message);
    }

    public void sendStudentRejectedEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Account Has Been Rejected");
        message.setText("Dear " + username + ", \n\n" +
                "Your student account has been rejected. You can contact admin on OneStop for inquiry.\n\n" +
                "Best regards,\nEduTrack");
        emailSender.send(message);
    }

    // Email sent to parent with credentials
    public void sendParentCredentialsEmail(String to, String username, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Parent Account Created");
        message.setText("Dear Parent, \n\n" +
                "Your parent account has been created. Below are your login details: \n" +
                "Username: " + username + "\n" +
                "Password: " + password + "\n\n" +
                "Please log in and change your password as soon as possible.");
        emailSender.send(message);
    }

    // New email function to notify students about a new assignment
    public void sendAssignmentNotification(String to, String username, String assignmentTitle, LocalDate dueDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("New Assignment Created: " + assignmentTitle);
        message.setText("Dear " + username + ", \n\n" +
                "A new assignment titled '" + assignmentTitle + "' has been created for your course. The due date for this assignment is: " + dueDate + ". \n\n" +
                "Please make sure to submit it before the deadline.\n\n" +
                "Best regards,\nYour Instructor");
        emailSender.send(message);
    }

    // New email function to notify students about assignment grading
    public void sendAssignmentGradedNotification(String to, String username, String assignmentTitle, int marks, String feedback) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Assignment '" + assignmentTitle + "' Has Been Graded");
        message.setText("Dear " + username + ", \n\n" +
                "Your assignment titled '" + assignmentTitle + "' has been graded. \n" +
                "Marks: " + marks + "/100 \n" +
                "Feedback: " + feedback + "\n\n" +
                "Please check your assignment for more details.\n\n" +
                "Best regards,\nYour Instructor");
        emailSender.send(message);
    }

    // Notify the parent about their child's graded assignment
    public void sendAssignmentGradedNotificationToParent(String to, String parentUsername, String studentUsername, String assignmentTitle, int marks, String feedback) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Child's Assignment '" + assignmentTitle + "' Has Been Graded");
        message.setText("Dear " + parentUsername + ", \n\n" +
                "Your child " + studentUsername + "'s assignment titled '" + assignmentTitle + "' has been graded. \n" +
                "Marks: " + marks + "/100 \n" +
                "Feedback: " + feedback + "\n\n" +
                "Please check with your child for more details.\n\n" +
                "Best regards,\nYour Instructor");
        emailSender.send(message);
    }

    // Method to send assignment submission reminder to students
    public void sendAssignmentReminder(String to, String username, String assignmentTitle, LocalDate dueDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reminder: Assignment Due Tomorrow");
        message.setText("Dear " + username + ", \n\n" +
                "This is a reminder that your assignment '" + assignmentTitle + "' is due on " + dueDate + ". \n\n" +
                "Please ensure that you submit it before the deadline.\n\n" +
                "Best regards,\nEduTrack");

        emailSender.send(message);
    }

    // Send book return reminder to the student
    public void sendBookReturnReminder(String to, String username, String bookTitle, LocalDate returnDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reminder: Book Return Due Tomorrow");
        message.setText("Dear " + username + ", \n\n" +
                "This is a reminder that the book titled '" + bookTitle + "' is due for return on " + returnDate + ". \n" +
                "Please return it to the library to avoid penalties.\n\n" +
                "Best regards,\nYour Library");

        emailSender.send(message);
    }
}
