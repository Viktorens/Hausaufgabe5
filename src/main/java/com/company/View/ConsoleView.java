package com.company.View;

import com.company.Controller.RegistrationSystem;
import com.company.Exceptions.InputException;
import com.company.Exceptions.NullException;
import com.company.Model.Course;
import com.company.Model.Student;
import com.company.Model.Teacher;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class ConsoleView {

    // stores the current logged In student or teacher
    private final RegistrationSystem controller;
    private final Scanner in;
    private Long loggedStudentId;
    private Long loggedTeacherId;

    public ConsoleView(RegistrationSystem regSystem) {
        this.controller = regSystem;
        in = new Scanner(System.in);
        loggedStudentId = null;
        loggedTeacherId = null;
    }

    /**
     * shows the options of the Main Menu
     */
    public void printStartMenu() {
        System.out.println("LOG IN");
        System.out.println("1. Student Menu");
        System.out.println("2. Teacher Menu");
        System.out.println("0. Exit.");
    }

    /**
     * log In by ID for the student
     *
     * @return true if the studentId exists in the repository, else false
     */
    public boolean logInStudent() {
        System.out.println("LOG IN STUDENT");
        System.out.println("Enter your ID: ");
        long studId = in.nextLong();
        try {
            if (this.controller.findOneStudent(studId) == null) {
                System.out.println("Error! Incorrect ID!");
                return false;
            } else
                this.loggedStudentId = studId;
            return true;
        } catch (NullException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
            return false;
        }
    }

    /**
     * log In by ID for the Teacher
     *
     * @return true if the teacherId exists in the repository, else false
     */
    public boolean logInTeacher() {
        System.out.println("LOG IN TEACHER");
        System.out.println("Enter your ID: ");
        long teacherId = in.nextLong();
        try {
            if (this.controller.findOneTeacher(teacherId) == null) {
                System.out.println("Error! Incorrect ID!");
                return false;
            } else
                this.loggedTeacherId = teacherId;
            return true;
        } catch (NullException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
            return false;
        }
    }

    /**
     * shows Menu for the student
     */
    public void printStudentMenu() {
        System.out.println("STUDENT MENU");
        try {
            Student loggedInStudent = this.controller.findOneStudent(loggedStudentId);
            System.out.println("Welcome, " + loggedInStudent.getFirstName() + " " + loggedInStudent.getLastName());
            System.out.println("Your status: ");
            System.out.println("Your Credits: " + loggedInStudent.getTotalCredits());
            System.out.println("Your enrolled courses: ");
            Stream.of(loggedInStudent.getEnrolledCourses())
                    .forEach(System.out::println);
            System.out.println();
            System.out.println("1. Enroll to a course");
            System.out.println("2. Show available courses");
            System.out.println("3. Sort courses by number credits");
            System.out.println("4. Filter courses by > 10 credits");
            System.out.println("0. Exit.");
        } catch (NullException e) {
            System.out.println(e.getMessage());
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
        }
    }

    /**
     * shows Menu for the teacher
     */
    public void printTeacherMenu() {
        System.out.println("TEACHER MENU");
        try {

            Teacher loggedInTeacher = this.controller.findOneTeacher(loggedTeacherId);
            System.out.println("Welcome, " + loggedInTeacher.getFirstName() + " " + loggedInTeacher.getLastName());
            System.out.println("Your status: ");
            System.out.println("Your courses: ");
            Stream.of(loggedInTeacher.getCourses())
                    .forEach(System.out::println);
            System.out.println();
            System.out.println("1. Enroll a student to a course");
            System.out.println("2. Show students enrolled to a given course");
            System.out.println("3. Sort students by name");
            System.out.println("4. Filter students by maximal credits number");
            System.out.println();
            System.out.println("5. Show available courses and the number of places");
            System.out.println("6. Add a new course");
            System.out.println("7. Update a course credits number");
            System.out.println("8. Delete a course");
            System.out.println("9. Sort courses by number credits");
            System.out.println("10. Filter courses by > 10 credits");
            System.out.println("0. Exit.");
        } catch (NullException e) {
            System.out.println(e.getMessage());
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
        }
    }

    /**
     * validates that the input is a Long number
     *
     * @param message shows the message when asking for Input
     * @return the Long number given by the user
     */
    public Long validateNumberInput(String message) {
        long nr = 0;
        boolean option;
        do {
            option = true;
            try {
                System.out.print(message);
                nr = in.nextLong();
            } catch (InputMismatchException e) {
                System.out.println("Wrong number! Please Try again.");
                option = false;
                in.reset();
                in.next();
            }
        } while (!option);
        return nr;
    }

    /**
     * finds the student with the given id
     * if the input id does not belong to any student in the repository, the input repeats
     *
     * @return the student that has id the input number
     */
    public Student validateStudentInput() {

        boolean validStudentId;
        Student givenStudent = null;
        do {
            validStudentId = true;
            long stud_id = this.validateNumberInput("\nChoose the ID of the student you want to enroll: ");
            try {
                givenStudent = controller.findOneStudent(stud_id);
                if (givenStudent == null)
                    validStudentId = false;
            } catch (NullException e) {
                System.out.println(e.getMessage());
                validStudentId = false;
            } catch (SQLException s) {
                System.out.println("Error! Unsuccessful connection to Database.");
            }
        } while (!validStudentId);
        return givenStudent;
    }

    /**
     * finds the course with the given id
     * if the input id does not belong to any courses in the repository, the input repeats
     *
     * @return the Course that has id the input number
     */
    public Course validateCourseInput() {
        Course givenCourse = null;
        boolean validCourseId;
        do {
            validCourseId = true;
            long course_id = this.validateNumberInput("\nChoose the ID of your course: ");
            try {
                givenCourse = controller.findOneCourse(course_id);
                if (givenCourse == null)
                    validCourseId = false;
            } catch (NullException e) {
                System.out.println(e.getMessage());
                validCourseId = false;
            } catch (SQLException s) {
                System.out.println("Error! Unsuccessful connection to Database.");
            }
        } while (!validCourseId);
        return givenCourse;
    }

    /**
     * gets input from the user the Student id and the Course id, validates it and enrolls the student
     */
    public void option1() {
        Student givenStudent = null;
        if (loggedStudentId == null) {
            System.out.println();
            try {
                Stream.of(controller.getAllStudents())
                        .forEach(System.out::println);
            } catch (SQLException s) {
                System.out.println("Error! Unsuccessful connection to Database1.");
            }
            givenStudent = this.validateStudentInput();
        } else {
            try {
                givenStudent = this.controller.findOneStudent(loggedStudentId);
            } catch (NullException e) {
                System.out.println(e.getMessage());
            } catch (SQLException s) {
                System.out.println("Error! Unsuccessful connection to Database2.");
            }
        }
        try {
            System.out.println();
            Stream.of(controller.getAllCourses())
                    .forEach(System.out::println);
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database3.");
        }
        Course givenCourse = this.validateCourseInput();
        try {
            controller.register(givenCourse, givenStudent);
            System.out.println("\nSuccessfully enrolled " + givenStudent.getFirstName() + " " + givenStudent.getLastName() + " to course: " + givenCourse.getName());
        } catch (NullException | InputException e) {
            System.out.println(e.getMessage());
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database4.");
        }
    }

    /**
     * gets input from the user the course id, validates it and shows the students
     */
    public void option2() {
        System.out.println();
        try {
            Stream.of(controller.getAllCourses())
                    .forEach(System.out::println);
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
        }
        Course searchedCourse = this.validateCourseInput();
        try {
            Stream.of(controller.retrieveStudentsEnrolledForACourse(searchedCourse))
                    .forEach(System.out::println);
            System.out.println();
        } catch (NullException | InputException e) {
            System.out.println(e.getMessage());
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
        }

    }

    /**
     * shows the list of students sorted ascending by last name and first name
     */
    public void option3() {
        System.out.println();
        try {
            Stream.of(controller.sortStudents())
                    .forEach(System.out::println);

        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
        }
    }

    /**
     * shows the list of students filtered by having maximum total credits number
     */
    public void option4() {
        System.out.println();
        try {
            Stream.of(controller.filterStudents())
                    .forEach(System.out::println);
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
        }
    }

    /**
     * shows the courses with free places
     */
    public void option5() {
        int freePlaces;
        try {
            for (Course course : controller.retrieveCoursesWithFreePlaces()) {
                freePlaces = course.getMaxEnrollment() - course.getStudentsEnrolled().size();
                System.out.println(freePlaces + " free places in: " + course);
            }
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
        }
        System.out.println();
    }

    /**
     * Adds a new course to the Course Repo and adds the teacher or updates his courses list
     */
    public void option6() {
        Teacher newCourseTeacher = null;
        String answear;
        do {
            System.out.println("Is the teacher of the course new? Y/N");
            answear = in.next();
        } while (!answear.equals("Y") && !(answear.equals("N")));
        if (answear.equals("Y")) {
            System.out.println("Enter the first name: ");
            String newTeacherFirstName = in.next();
            System.out.println("Enter the last name: ");
            String newTeacherLastName = in.next();
            try {
                long newTeacherId = ((long) controller.getAllTeachers().size()) + 1;
                newCourseTeacher = new Teacher(newTeacherId, newTeacherFirstName, newTeacherLastName);
            } catch (SQLException s) {
                System.out.println("Error! Unsuccessful connection to Database.");
            }
        } else {
            try {
                newCourseTeacher = this.controller.findOneTeacher(this.loggedTeacherId);
            } catch (NullException e) {
                System.out.println(e.getMessage());
            } catch (SQLException s) {
                System.out.println("Error! Unsuccessful connection to Database.");
            }
        }
        System.out.println("Enter the name of the course: ");
        String newCourseName = in.next();

        System.out.println("Enter the credits number of the course: ");
        int newCourseCredits = in.nextInt();

        System.out.println("Enter the maximum enrollment number of the course: ");
        int newCourseMaxEnrollment = in.nextInt();
        try {
            int numberCourses = controller.getAllCourses().size();
            long lastCourseId = ((long) controller.getAllCourses().get(numberCourses - 1).getCourseId());
            long newCourseId = lastCourseId + 1;
            Course newCourse = new Course(newCourseId, newCourseName, newCourseTeacher, newCourseMaxEnrollment, newCourseCredits);
            try {
                System.out.println(newCourse);
                controller.addCourse(newCourse);
            } catch (NullException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
        }
    }

    /**
     * teacher can modify the credits number for one of his courses and input is validated
     */
    public void option7() {
        System.out.println();
        try {
            Stream.of(controller.findOneTeacher(loggedTeacherId).getCourses())
                    .forEach(System.out::println);
        } catch (NullException e) {
            System.out.println(e.getMessage());
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
        }
        Course foundCourse = this.validateCourseInput();
        int new_credits = 0;
        boolean okCredits = true;
        do {
            System.out.println("\nEnter the new number of credits: ");
            try {
                new_credits = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error! Wrong input number.");
                okCredits = false;
                in.reset();
                in.next();
            }
            if (new_credits < 0)
                okCredits = false;
        } while (!okCredits);
        foundCourse.setCredits(new_credits);
        try {
            controller.modifyCredits(foundCourse);
        } catch (NullException e) {
            System.out.println(e.getMessage());
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
        }
    }

    /**
     * Delete a course from the logged in Teacher
     */
    public void option8() {
        Teacher givenTeacher = null;
        try {
            givenTeacher = this.controller.findOneTeacher(this.loggedTeacherId);
        } catch (NullException e) {
            System.out.println(e.getMessage());
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
        }
        assert givenTeacher != null;
        List<Course> courseList = givenTeacher.getCourses();
        for (Course c : courseList) {
            System.out.println(c.getCourseId() + "  " + c.getName());
        }
        Course choosenCourse = this.validateCourseInput();
        try {
            if (controller.deleteCourseFromTeacher(givenTeacher, choosenCourse)) {
                System.out.println("Course was deleted from teacher " + givenTeacher.getFirstName());
            }
        } catch (NullException | InputException e) {
            System.out.println(e.getMessage());
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
        }
        System.out.println();
    }

    /**
     * shows the list of courses sorted ascending by credits number
     */
    public void option9() {
        System.out.println();
        try {
            Stream.of(controller.sortCourses())
                    .forEach(System.out::println);
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
        }
    }

    /**
     * shows the list of courses filtered by having more than 10 credits
     */
    public void option10() {
        System.out.println();
        try {
            Stream.of(controller.filterCourses())
                    .forEach(System.out::println);
        } catch (SQLException s) {
            System.out.println("Error! Unsuccessful connection to Database.");
        }
    }


    /**
     * Teacher Menu with actions
     */
    public void teacherMenu() {
        boolean stay = true;
        int key = 0;
        while (stay) {
            boolean option;
            do {
                printTeacherMenu();
                option = true;
                try {
                    System.out.print("Enter your option: ");
                    key = in.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Wrong number! Try again.");
                    option = false;
                    in.reset();
                    in.next();
                }
            } while (!option);
            switch (key) {
                //Exit, program ends
                case 0:
                    System.out.println("Goodbye!");

                    stay = false;
                    break;

                // Enroll a student to a course
                case 1:
                    this.option1();
                    break;

                // Show students enrolled to a given course
                case 2:
                    this.option2();
                    break;

                // Sort students by name
                case 3:
                    this.option3();
                    break;

                // Filter students by maximal credits number (30)
                case 4:
                    this.option4();
                    break;

                // Show available courses and the number of places
                case 5:
                    this.option5();
                    break;

                // Add a new course
                case 6:
                    this.option6();
                    break;

                // Update a course with a new credits number
                case 7:
                    this.option7();
                    break;

                // Delete a given course
                case 8:
                    this.option8();
                    break;

                // Sort courses by number credits
                case 9:
                    this.option9();
                    break;

                // Filter courses by > 10 credits
                case 10:
                    this.option10();
                    break;
            }
        }
    }

    /**
     * Student Menu with actions
     */
    public void studentMenu() {
        boolean stay = true;
        int key = 0;
        while (stay) {
            boolean option;
            do {
                printStudentMenu();
                option = true;
                try {
                    System.out.print("Enter your option: ");
                    key = in.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Wrong number! Try again.");
                    option = false;
                    in.reset();
                    in.next();
                }
            } while (!option);
            switch (key) {
                // Exit, program ends
                case 0:
                    System.out.println("Goodbye!");

                    stay = false;
                    break;

                // Enroll a student to a course
                case 1:
                    this.option1();
                    break;

                // Show available courses and the number of places
                case 2:
                    this.option5();
                    break;

                // Sort courses by number credits
                case 3:
                    this.option9();
                    break;

                // Filter courses by > 10 credits
                case 4:
                    this.option10();
                    break;
            }
        }
    }

    /**
     * The (Start) Main Menu
     */
    public void menu() {
        boolean stay = true;
        int key = 0;
        while (stay) {
            //validating input (has to be an int number between 0 and 10)
            boolean option;
            do {
                printStartMenu();
                option = true;
                try {
                    System.out.print("Enter your option: ");
                    key = in.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Wrong number! Try again.");
                    option = false;
                    in.reset();
                    in.next();
                }
            } while (!option);
            switch (key) {
                // Exit, program ends
                case 0:
                    System.out.println("Goodbye!");

                    stay = false;
                    break;

                // LogIn Student Menu
                case 1:
                    if (logInStudent())
                        this.studentMenu();
                    this.loggedStudentId = null;
                    break;

                // LogIn Teacher Menu
                case 2:
                    if (logInTeacher())
                        this.teacherMenu();
                    this.loggedTeacherId = null;
                    break;
            }
        }
    }
}
