package com.fifth_semester.project.services;

import com.fifth_semester.project.entities.*;
import com.fifth_semester.project.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.SecureRandom;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TimetableService {

    private static final Logger logger = LoggerFactory.getLogger(TimetableService.class);

    @Autowired
    private ClassScheduleRepository classScheduleRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SectionRepository sectionRepository;

    @Transactional
    public String uploadTimetable(MultipartFile file) throws IOException {
        classScheduleRepository.truncateMyEntityTable();
        logger.info("Starting upload timetable process...");

        // Save the uploaded file to a temporary location
        String tempFilePath = "D:/sda project/project/project/src/main/resources/scripts/" + file.getOriginalFilename();
        logger.info("Temporary file path: {}", tempFilePath);

        try {
            file.transferTo(new File(tempFilePath));
            logger.info("File saved successfully at path: {}", tempFilePath);
        } catch (IOException e) {
            logger.error("Failed to save file: {}", e.getMessage(), e);
            throw e;
        }

        // Call the parsing function
        try {
            List<ClassSchedule> classSchedules = parseTimetableWithPython(tempFilePath);
            logger.info("Parsed timetable successfully. Saving schedules to the database...");
            classScheduleRepository.saveAll(classSchedules);
        } catch (Exception e) {
            logger.error("Failed during timetable parsing: {}", e.getMessage(), e);
            throw e;
        }

        return "Timetable uploaded and schedules saved successfully.";
    }

    private List<ClassSchedule> parseTimetableWithPython(String timetableFilePath) {
        List<ClassSchedule> classSchedules = new ArrayList<>();

        try {
            String pythonScriptPath = "D:/sda project/project/project/src/main/resources/scripts/main.py";

            List<String> command = new ArrayList<>();
            command.add("python");
            command.add(pythonScriptPath);
            command.add(timetableFilePath);

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File("D:/sda project/project/project/src/main/resources/scripts"));
            pb.redirectErrorStream(true);

            Map<String, String> env = pb.environment();
            env.put("PYTHONIOENCODING", "UTF-8"); // Ensure UTF-8 encoding


            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            StringBuilder output = new StringBuilder();
            String line;

//            logger.info("Reading output from Python script...");

            while ((line = reader.readLine()) != null) {
                output.append(line);
            }


            int exitCode = process.waitFor();

            if (exitCode != 0) {
                logger.error("Python script exited with code: {}", exitCode);
                throw new RuntimeException("Python script failed with exit code " + exitCode);
            }

            String jsonData = output.toString();
//            logger.info("Received JSON data: {}", jsonData);


            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> classDataList = objectMapper.readValue(jsonData, new TypeReference<List<Map<String, Object>>>(){});


            for (Map<String, Object> classData : classDataList) {
                String courseCode = (String) classData.get("course");
                String instructorName = (String) classData.get("instructor");
                String timeSlot = (String) classData.get("time_slot");
                String room = (String) classData.get("room");
                String weekday = ((String) classData.get("weekday")).toUpperCase();

                String[] times = timeSlot.split("-");
                if (times.length != 2) {
                    logger.error("Invalid time slot format: {}", timeSlot);
                    continue;
                }

                String startTimeStr = times[0].trim();
                String endTimeStr = times[1].trim();

                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                LocalTime startTime = LocalTime.parse(startTimeStr, timeFormatter);
                LocalTime endTime = LocalTime.parse(endTimeStr, timeFormatter);

                // For example, course code might be "SDA BCS-5A", where "BCS-5A" is the section
                String[] courseParts = courseCode.split(" ");
//                logger.info("courseParts[0]: "+ courseParts[0]);
//                logger.info("courseParts[1]: "+ courseParts[1]);
                String courseCodeShort = courseParts[0];
                String sectionName = courseParts[1];
                int semester = extractSemester(sectionName);
//                logger.info("semester: "+semester);
                String course_id = sectionName.substring(1,2) + '-' + courseCodeShort;
//                logger.info("course_id: "+course_id);
                sectionName = sectionName.substring(sectionName.length() - 1).toUpperCase();
//                logger.info("sectionName: "+sectionName);

                Optional<Course> courseOpt = courseRepository.findByCourseCode(course_id);
                Course course;
                if (courseOpt.isPresent()) {
                    course = courseOpt.get();
//                    logger.info("Attempted to insert duplicate course with ID: {}, Course Code: {}", course.getCourseId(), courseCodeShort);
//                    logger.info("Duplicate Course Code: {}", courseCodeShort);
//                    logger.info("Duplicate Course Name: {}", course.getCourseName());
//                    logger.info("Duplicate Course Semester: {}", course.getSemester());
//                    logger.info("Duplicate Course ID: {}", course.getCourseId());
                } else {
                    Course newCourse = new Course();
                    newCourse.setCourseCode(course_id);
                    newCourse.setCourseName(courseCodeShort);
                    newCourse.setSemester(semester);
                    newCourse.setCourseId(courseCodeShort);
                    newCourse.setCreditHours(3);
                    courseRepository.save(newCourse);
//                    logger.info("Created new course with code: {}", courseCodeShort);
//                    logger.info("New Course Code: {}", courseCodeShort);
//                    logger.info("New Course Name: {}", courseCodeShort);
//                    logger.info("New Course Semester: {}", semester);
//                    logger.info("New Course ID: {}", course_id);
                    course = newCourse;
                }


                Optional<Section> sectionOpt = sectionRepository.findByCourseAndSectionName(course, sectionName);
                Teacher teacher;
                        Section sectionObj;
                if (sectionOpt.isPresent()) {
                    sectionObj = sectionOpt.get();
                    teacher = handleTeacherCreation(instructorName,courseCode,sectionName);
                    sectionObj.setTeacher(teacher);
//                    logger.info("Found existing section for course {} with section name: {}", course.getCourseCode(), sectionName);
                } else {
//                    logger.info("Creating new section for course {} with section name: {}", course.getCourseCode(), sectionName);
                    sectionObj = new Section();
                    sectionObj.setCourse(course);
                    sectionObj.setSectionName(sectionName);
                    sectionRepository.save(sectionObj);
                    teacher = handleTeacherCreation(instructorName,courseCode,sectionName);
                    sectionObj.setTeacher(teacher);
                    sectionRepository.save(sectionObj);
                    logger.info("Created new section for course {} with section name: {}", course.getCourseCode(), sectionName);
                }
                logger.info("Teacher assigned to section {}: {}", sectionObj.getSectionName(), sectionObj.getTeacher().getUsername());
                ClassSchedule classSchedule = new ClassSchedule(course,sectionObj,teacher, startTime, endTime, room,  semester,weekday);
                try {
                    saveClassScheduleIfNotExists(classSchedule);
                } catch (DataIntegrityViolationException e) {
                    logger.error("Attempted to insert duplicate class schedule", e);
                }

            }

        } catch (IOException | InterruptedException e) {
            logger.error("Error while executing Python script: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to execute Python script: " + e.getMessage(), e);
        }

        return classSchedules;
    }

    private int extractSemester(String sectionName) {
        try {
            String[] parts = sectionName.split("-");
            if (parts.length >= 2) {
                String semesterPart = parts[1];
                String semesterStr = semesterPart.replaceAll("\\D", "");
                return Integer.parseInt(semesterStr);
            }
        } catch (Exception e) {
            logger.warn("Could not extract semester from section name: {}", sectionName);
        }
        return 0;
    }
    private String generateRandomPassword() {
        int length = 12;
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    private String generateValidUsername(String fullName) {
        String baseUsername = fullName.replaceAll("\\s+", "").toLowerCase();
        int maxUsernameLength = 20;
        if (baseUsername.length() > maxUsernameLength) {
            baseUsername = baseUsername.substring(0, maxUsernameLength);
        }
        return baseUsername;
    }
    private Teacher handleTeacherCreation(String instructorName, String courseCode, String sectionName) {
        String username = generateValidUsername(instructorName);
        String email = instructorName.replaceAll("\\s+", "").toLowerCase() + "@example.com";

        return userRepository.findByUsernameOrEmail(username, email)
                .map(user -> teacherRepository.findById(user.getId()).orElseThrow(() -> new IllegalStateException("User is not a teacher")))
                .orElseGet(() -> {
                    Teacher newTeacher = new Teacher();
                    newTeacher.setUsername(username);
                    newTeacher.setEmail(email);
                    newTeacher.setTeacherId(generateTeacherId());
                    newTeacher.setPassword(hashPassword("12345678"));  // Consider a more secure handling
                    teacherRepository.save(newTeacher);
                    logger.info("Created new teacher with username: {}", username);
                    return newTeacher;
                });
    }

    private String generateTeacherId() {
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(6);
        for (int i = 0; i < 2; i++) {
            char letter = (char) ('A' + random.nextInt(26));
            stringBuilder.append(letter);
        }
        for (int i = 0; i < 4; i++) {
            int number = random.nextInt(10);
            stringBuilder.append(number);
        }
        return stringBuilder.toString();
    }

    private String hashPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Transactional
    public void saveClassScheduleIfNotExists(ClassSchedule classSchedule) {
        Optional<ClassSchedule> existingSchedule = classScheduleRepository.findBySectionAndDayAndStartTimeAndEndTime(
                classSchedule.getSection(), classSchedule.getDay(), classSchedule.getStartTime(), classSchedule.getEndTime());

        if (existingSchedule.isPresent()) {
            logger.info("Class schedule already exists for section {} on {} at {}",
                    classSchedule.getSection().getSectionName(), classSchedule.getDay(), classSchedule.getStartTime());
        } else {
            classScheduleRepository.save(classSchedule);
            logger.info("Created new class schedule for section {} on {} at {}",
                    classSchedule.getSection().getSectionName(), classSchedule.getDay(), classSchedule.getStartTime());
        }
    }

}
