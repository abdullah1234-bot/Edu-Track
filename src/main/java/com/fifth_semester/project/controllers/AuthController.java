package com.fifth_semester.project.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.fifth_semester.project.dtos.request.*;
import com.fifth_semester.project.dtos.response.*;
import com.fifth_semester.project.repositories.*;
import com.fifth_semester.project.security.jwt.JwtUtils;
import com.fifth_semester.project.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.fifth_semester.project.entities.*;
import com.fifth_semester.project.services.EmailService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/auth")
@Tag(name = "Authentication APIs", description = "Register & Login User")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ParentRepository parentRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    EmailService emailService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    public AdminRepository adminRepository;

    @Autowired
    public StudentRepository studentRepository;

    @Autowired
    public LibrarianRepository librarianRepository;

    @Autowired
    public TeacherRepository teacherRepository;

    @Value("${project.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Shared login endpoint for all roles

    //  @GetMapping("/abc")
//  public String abc(){
//    String newp = "12345678";
//    User user = userRepository.findByEmail("basitfast681@gmail.com").orElseThrow(()->new RuntimeException("no user"));
//    user.setPassword(encoder.encode(newp));
//    userRepository.save(user);
//    return "abc";
//  }
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);


        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        if (roles.contains("ROLE_STUDENT")) {
            Student student = studentRepository.findByEmail(userDetails.getEmail())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            if (student.getAccountStatus() == AccountStatus.PENDING) {
                return ResponseEntity.badRequest().body("Student Registration is Pending");
            } else if (student.getAccountStatus() == AccountStatus.REJECTED) {
                return ResponseEntity.badRequest().body("Student Registration Rejected by Admin");
            }
        } else if (roles.contains("ROLE_PARENT")) {
            Parent parent = parentRepository.findByEmail(userDetails.getEmail())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));  // Fixed error message
            if (parent.getAccountStatus() == AccountStatus.PENDING) {  // Changed to PENDING
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                jwtExpirationMs));
    }

    // Admin registers a teacher
    @PostMapping("/register/teacher")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerTeacher(@Valid @RequestBody TeacherSignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new teacher account with ACTIVE status
        Teacher teacher = new Teacher(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getTeacherId(),
                signUpRequest.getDepartment(),
                signUpRequest.getOfficeHours(),
                signUpRequest.getDateOfHire(),
                signUpRequest.getQualification(),
                signUpRequest.getSpecialization()
        );
        teacher.setAccountStatus(AccountStatus.ACTIVE);  // Teachers are active by default

        // Assign ROLE_TEACHER
        Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(teacherRole);
        teacher.setRoles(roles);

        userRepository.save(teacher);

        return ResponseEntity.ok(new MessageResponse("Teacher registered successfully!"));
    }

    // Admin registers a librarian
    @PostMapping("/register/librarian")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerLibrarian(@Valid @RequestBody LibrarianSignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new librarian account with ACTIVE status
        Librarian librarian = new Librarian(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmployeeId(),
                signUpRequest.getLibrarySection()
        );
        librarian.setAccountStatus(AccountStatus.ACTIVE);  // Librarians are active by default

        // Assign ROLE_LIBRARIAN
        Role librarianRole = roleRepository.findByName(ERole.ROLE_LIBRARIAN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(librarianRole);
        librarian.setRoles(roles);

        userRepository.save(librarian);

        return ResponseEntity.ok(new MessageResponse("Librarian registered successfully!"));
    }

    // Admin registers another admin
    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminSignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new admin account with ACTIVE status
        Admin admin = new Admin(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getAdminId()
        );
        admin.setAccountStatus(AccountStatus.ACTIVE);  // Admins are active by default

        // Assign ROLE_ADMIN
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        admin.setRoles(roles);

        userRepository.save(admin);

        return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody StudentSignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new student account with 'PENDING' status
        Student student = new Student(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getAcademicYear(),
                signUpRequest.getSemester()
        );
        student.setStudentId(signUpRequest.getStudentId());
        student.setEmergencyContact(signUpRequest.getParentContactNumber());
        student.setDateOfBirth(signUpRequest.getDob());
        student.setAddress(signUpRequest.getParentAddress());
        student.setAccountStatus(AccountStatus.PENDING);  // Student needs admin approval

        // Assign ROLE_STUDENT
        Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(studentRole);
        student.setRoles(roles);
        String temporaryPassword = encoder.encode("tempPassword");
        // Save parent information in the Student entity to be used during approval

        Parent parent;
        Optional<Parent> parentOpt = parentRepository.findByEmail(signUpRequest.getParentEmail());
        if (parentOpt.isPresent()) {
            parent = parentOpt.get();
        } else {
            parent = new Parent(
                    signUpRequest.getParentUsername(),
                    signUpRequest.getParentEmail(),
                    temporaryPassword,  // Password will be generated later
                    signUpRequest.getParentContactNumber(),
                    signUpRequest.getParentAddress(),
                    signUpRequest.getParentOccupation()
            );


            parentRepository.save(parent);  // Save the parent first
            // Then associate with student
        }
        parent.setAccountStatus(AccountStatus.PENDING);
        student.setParent(parent);
        // Save the student
        userRepository.save(student);

        return ResponseEntity.ok(new MessageResponse("Student registered successfully! Awaiting admin approval."));
    }

    // Admin approves pending student registration and creates a Parent account
    @Transactional
    @PostMapping("/approve/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveStudent(@PathVariable Long studentId) {
        // Find student by ID
        Student student = (Student) userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Error: Student not found"));

        // Ensure the student's status is still pending
        if (!student.getAccountStatus().equals(AccountStatus.PENDING)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Student has already been approved or rejected."));
        }

        // Set student status to ACTIVE
        student.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(student);

        // Create a Parent account for the student
        Parent parent = createParentForStudent(student);
        userRepository.save(parent);  // Save the parent user in the database

        // Send emails to both student and parent
        emailService.sendStudentApprovalEmail(student.getEmail(), student.getUsername());

        // Save notifications for both student and parent
        createAndSaveNotification(student, "Your account has been approved.");

        return ResponseEntity.ok(new MessageResponse("Student approved and Parent account created successfully!"));
    }

    // Admin approves pending student registration and creates a Parent account
    @Transactional
    @PostMapping("/reject/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectStudent(@PathVariable Long studentId) {
        // Find student by ID
        Student student = (Student) userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Error: Student not found"));

        // Ensure the student's status is still pending
        if (!student.getAccountStatus().equals(AccountStatus.PENDING)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Student has already been approved or rejected."));
        }
        // Send emails to both student
        emailService.sendStudentRejectedEmail(student.getEmail(), student.getUsername());
        // Set student status to ACTIVE
        student.setAccountStatus(AccountStatus.REJECTED);
        userRepository.save(student);

        // Create a Parent account for the student
        Parent parent = student.getParent();
        if (parent.getChildren().size() == 1) {
            parent.setAccountStatus(AccountStatus.REJECTED);
            userRepository.save(parent);
        }

//        userRepository.save(parent);  // Save the parent user in the database

        return ResponseEntity.ok(new MessageResponse("Student rejected!"));
    }

    private Parent createParentForStudent(Student student) {
        // Parent information is already stored in the Student entity
        Parent parent = student.getParent();

        // Generate a random password for the parent
        String randomPassword = UUID.randomUUID().toString().substring(0, 8);  // Generate 8-character password
        String encodedPassword = encoder.encode(randomPassword);  // Encrypt the password

        // Set the generated password in the Parent entity
        parent.setPassword(encodedPassword);

        // Set the Parent's account status to ACTIVE
        parent.setAccountStatus(AccountStatus.ACTIVE);

        // Assign ROLE_PARENT to the parent
        Role parentRole = roleRepository.findByName(ERole.ROLE_PARENT)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(parentRole);
        parent.setRoles(roles);
        emailService.sendParentCredentialsEmail(parent.getEmail(), parent.getUsername(), randomPassword);
        createAndSaveNotification(parent, "Parent account created for student. with email: " + parent.getEmail() + " and password: " + randomPassword);

        return parent;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getadmininfo")
    public ResponseEntity<InfoAdminDTO> getAdminInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Admin admin = adminRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        InfoAdminDTO info = adminRepository.getAdminInfo(admin);
        if (info != null) {
            return ResponseEntity.ok(info); // Return 200 OK with the parent info
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/getteacherinfo")
    public ResponseEntity<InfoTeacherDTO> getTeacherInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Teacher teacher = teacherRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        InfoTeacherDTO info = teacherRepository.getTeacherInfo(teacher);
        if (info != null) {
            return ResponseEntity.ok(info); // Return 200 OK with the parent info
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/getlibrarianinfo")
    public ResponseEntity<InfoLibrarianDTO> getLibrarianInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Librarian librarian = librarianRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Librarian not found"));
        InfoLibrarianDTO info = librarianRepository.getLibrarianInfo(librarian);
        if (info != null) {
            return ResponseEntity.ok(info); // Return 200 OK with the parent info
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/getstudentinfo")
    public ResponseEntity<InfoStudentDTO> getStudentInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Student student = studentRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        InfoStudentDTO info = studentRepository.getStudentInfo(student);
        if (info != null) {
            return ResponseEntity.ok(info);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PreAuthorize("hasRole('PARENT')")
    @GetMapping("/getparentinfo")
    public ResponseEntity<InfoParentDTO> getParentInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Parent parent = parentRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        InfoParentDTO info = parentRepository.getParentInfo(parent.getEmail());
        if (info != null) {
            List<StudentDTO> children = studentRepository.findStudentDTOsByParentId(info.getId()).stream()
                    .map(student -> new StudentDTO(student.getId(), student.getEmail(), student.getStudentId(), student.getUsername()))
                    .collect(Collectors.toList());
            info.setChildren(children);
            return ResponseEntity.ok(info); // Return 200 OK with the parent info
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }

    // Helper method to create and save a notification
    private void createAndSaveNotification(User recipient, String message) {
        Notification notification = new Notification();
        notification.setUser(recipient);
        notification.setMessage(message);
        notification.setNotificationDate(java.time.LocalDateTime.now());
        notification.setNotificationType(NotificationType.ACCOUNT_APPROVAL);  // Assuming we have an enum for this
        notification.setStatus(NotificationStatus.UNREAD);

        notificationRepository.save(notification);  // Save the notification
    }
}
