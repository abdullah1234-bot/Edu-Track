package com.fifth_semester.project.services;

import com.fifth_semester.project.dtos.response.TimetableRow;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelParsingService {

    public List<TimetableRow> parseTimetableExcel(MultipartFile file) throws IOException {
        List<TimetableRow> timetableRows = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);  // Assuming we're reading from the first sheet
            DataFormatter dataFormatter = new DataFormatter();

            // Iterate through each row in the sheet
            for (int i = 4; i < sheet.getPhysicalNumberOfRows(); i++) {  // Start from 4th row, skipping headers
                Row row = sheet.getRow(i);

                if (row == null) continue;

                String venue = dataFormatter.formatCellValue(row.getCell(0));  // Venue
                for (int j = 1; j < row.getPhysicalNumberOfCells(); j++) {  // Iterate over time slots
                    String courseInfo = dataFormatter.formatCellValue(row.getCell(j));
                    if (!courseInfo.isEmpty()) {
                        TimetableRow timetableRow = new TimetableRow();

                        // Extract course code, section, instructor from courseInfo
                        String[] courseDetails = courseInfo.split("\n");
                        timetableRow.setCourseCode(courseDetails[0].split(" ")[0]);
                        timetableRow.setSection(courseDetails[0].split(" ")[1]);
                        timetableRow.setInstructor(courseDetails[1]);

                        // Time slot and venue from the excel sheet
                        timetableRow.setTimeSlot(dataFormatter.formatCellValue(sheet.getRow(2).getCell(j)));
                        timetableRow.setVenue(venue);

                        // Add to list
                        timetableRows.add(timetableRow);
                    }
                }
            }
        }

        return timetableRows;
    }
}
