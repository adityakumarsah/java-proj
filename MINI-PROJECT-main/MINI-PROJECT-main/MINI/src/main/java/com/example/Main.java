package com.example;

import com.example.config.AppConfig;
import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.model.Course;
import com.example.model.Student;
import com.example.service.FeeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static StudentDAO studentDAO;
    private static CourseDAO courseDAO;
    private static FeeService feeService;
    private static Scanner scanner;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        
        studentDAO = context.getBean(StudentDAO.class);
        courseDAO = context.getBean(CourseDAO.class);
        feeService = context.getBean(FeeService.class);
        scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        viewAllStudents();
                        break;
                    case 3:
                        updateStudent();
                        break;
                    case 4:
                        deleteStudent();
                        break;
                    case 5:
                        processPayment();
                        break;
                    case 6:
                        processRefund();
                        break;
                    case 7:
                        addCourse();
                        break;
                    case 8:
                        viewAllCourses();
                        break;
                    case 9:
                        System.out.println("Exiting...");
                        context.close();
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Student Management System ===");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Update Student");
        System.out.println("4. Delete Student");
        System.out.println("5. Process Payment");
        System.out.println("6. Process Refund");
        System.out.println("7. Add Course");
        System.out.println("8. View All Courses");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addStudent() {
        try {
            System.out.print("Enter student name: ");
            String name = scanner.nextLine();

            viewAllCourses();
            System.out.print("Enter course ID: ");
            Long courseId = Long.parseLong(scanner.nextLine());
            Course course = courseDAO.findById(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Course not found"));

            Student student = new Student(name, course);
            studentDAO.save(student);
            System.out.println("Student added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid course ID! Please enter a valid number.");
        }
    }

    private static void viewAllStudents() {
        List<Student> students = studentDAO.findAll();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        students.forEach(student -> {
            System.out.println("\nID: " + student.getStudentId());
            System.out.println("Name: " + student.getName());
            System.out.println("Course: " + student.getCourse().getCourseName());
            System.out.println("Balance: $" + student.getBalance());
        });
    }

    private static void updateStudent() {
        try {
            viewAllStudents();
            System.out.print("Enter student ID to update: ");
            Long id = Long.parseLong(scanner.nextLine());

            Student student = studentDAO.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));

            System.out.print("Enter new name (or press enter to skip): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                student.setName(name);
            }

            System.out.print("Do you want to change the course? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                viewAllCourses();
                System.out.print("Enter new course ID: ");
                Long courseId = Long.parseLong(scanner.nextLine());
                Course course = courseDAO.findById(courseId)
                        .orElseThrow(() -> new IllegalArgumentException("Course not found"));
                student.setCourse(course);
            }

            studentDAO.update(student);
            System.out.println("Student updated successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter valid numbers.");
        }
    }

    private static void deleteStudent() {
        try {
            viewAllStudents();
            System.out.print("Enter student ID to delete: ");
            Long id = Long.parseLong(scanner.nextLine());

            Student student = studentDAO.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));

            studentDAO.delete(student);
            System.out.println("Student deleted successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID! Please enter a valid number.");
        }
    }

    private static void processPayment() {
        try {
            viewAllStudents();
            System.out.print("Enter student ID: ");
            Long id = Long.parseLong(scanner.nextLine());

            Student student = studentDAO.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));

            System.out.print("Enter payment amount: $");
            BigDecimal amount = new BigDecimal(scanner.nextLine());

            feeService.processPayment(student, amount);
            System.out.println("Payment processed successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter valid numbers.");
        }
    }

    private static void processRefund() {
        try {
            viewAllStudents();
            System.out.print("Enter student ID: ");
            Long id = Long.parseLong(scanner.nextLine());

            Student student = studentDAO.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));

            System.out.print("Enter refund amount: $");
            BigDecimal amount = new BigDecimal(scanner.nextLine());

            feeService.processRefund(student, amount);
            System.out.println("Refund processed successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter valid numbers.");
        }
    }

    private static void addCourse() {
        System.out.print("Enter course name: ");
        String name = scanner.nextLine();

        System.out.print("Enter course duration: ");
        String duration = scanner.nextLine();

        Course course = new Course(name, duration);
        courseDAO.save(course);
        System.out.println("Course added successfully!");
    }

    private static void viewAllCourses() {
        List<Course> courses = courseDAO.findAll();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        courses.forEach(course -> {
            System.out.println("\nID: " + course.getCourseId());
            System.out.println("Name: " + course.getCourseName());
            System.out.println("Duration: " + course.getDuration());
        });
    }
}