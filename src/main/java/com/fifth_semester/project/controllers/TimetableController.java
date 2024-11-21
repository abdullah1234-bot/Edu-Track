package com.fifth_semester.project.controllers;

import com.fifth_semester.project.dtos.response.MessageResponse;
import com.fifth_semester.project.services.TimetableService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/timetable")
@Tag(name = "Timetable Upload API")
public class TimetableController {

    private static final Logger logger = LoggerFactory.getLogger(TimetableController.class);

    @Autowired
    private TimetableService timetableService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadTimetable(@RequestParam("file") MultipartFile file) {
        try {
            timetableService.uploadTimetable(file);
            return ResponseEntity.ok("Timetable uploaded and schedules saved successfully.");
        } catch (MultipartException e) {
            logger.error("Multipart exception: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("Invalid file upload request.");
        } catch (Exception e) {
            logger.error("General exception: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File processing failed.");
        }

    }

}
