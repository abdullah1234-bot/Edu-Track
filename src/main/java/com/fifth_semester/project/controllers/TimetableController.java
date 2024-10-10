package com.fifth_semester.project.controllers;

import com.fifth_semester.project.dtos.response.MessageResponse;
import com.fifth_semester.project.dtos.response.TimetableRow;
import com.fifth_semester.project.services.ExcelParsingService;
import com.fifth_semester.project.services.TimetableService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@Tag(name = "Timetable Upload API")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;

    @Autowired
    private ExcelParsingService excelParsingService;

    // Endpoint to upload the timetable sheet
    @PostMapping("/upload")
    public ResponseEntity<?> uploadTimetable(@RequestParam("file") MultipartFile file) {
        try {
            // Parse the file
            List<TimetableRow> timetableRows = excelParsingService.parseTimetableExcel(file);

            // Call the service to upload and save the timetable
            String result = timetableService.uploadTimetable(timetableRows);

            return ResponseEntity.ok(new MessageResponse(result));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to parse the file"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
