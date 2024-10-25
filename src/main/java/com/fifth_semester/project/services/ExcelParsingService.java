package com.fifth_semester.project.services;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ExcelParsingService {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TimetableParser <file_path>");
            return;
        }

        String filePath = args[0];
        parseTimetable(filePath);
    }

    public static void parseTimetable(String filePath) {
        List<String> validDays = Arrays.asList("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY");

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName().trim().toUpperCase();

                if (validDays.contains(sheetName)) {
                    for (int rowIndex = 3; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                        Row row = sheet.getRow(rowIndex);

                        if (row != null) {
                            String course = getCellValue(row.getCell(0));
                            String instructor = getCellValue(row.getCell(1));
                            String startTime = getCellValue(row.getCell(2));
                            String endTime = getCellValue(row.getCell(3));
                            String room = getCellValue(row.getCell(4));
                            String section = getCellValue(row.getCell(5));
                            String semester = getCellValue(row.getCell(6));

                            // Print the result in the specified format
                            System.out.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                                    course, instructor, sheetName, startTime, endTime, room, section, semester);
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalTime().toString(); // For time values
                } else {
                    return String.valueOf((int) cell.getNumericCellValue()); // Assuming numbers are integers
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
