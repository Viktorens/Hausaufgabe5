package com.company.Controller;

import com.company.Exceptions.InputException;
import com.company.Exceptions.NullException;
import com.company.Model.Course;
import com.company.Model.Student;
import com.company.Model.Teacher;
import com.company.Repository.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class RegistrationSystem {
    private StudentRepository studentsRepo;
    private TeacherRepository teachersRepo;
    private CourseRepository coursesRepo;

    public RegistrationSystem(StudentRepository studentsRepo,
                              TeacherRepository teachersRepo,
                              CourseRepository coursesRepo) {
        this.studentsRepo = studentsRepo;
        this.teachersRepo = teachersRepo;
        this.coursesRepo = coursesRepo;
    }


    /**
     * sorts the Student's repository list by name
     *
     * @return the sorted list
     */
    public List<Student> sortStudents() throws SQLException {
        List<Student> sortedStudents = this.getAllStudents()
                .stream()
                .sorted(Comparator.comparing(Student::getLastName)
                        .thenComparing(Student::getFirstName))
                .collect(Collectors.toList());
        return sortedStudents;
    }

    /**
     * sorts the Course repository list by credits number ascending
     *
     * @return the sorted list
     */
    public List<Course> sortCourses() throws SQLException {
        List<Course> sortedCourses = this.getAllCourses()
                .stream()
                .sorted(Comparator.comparing(Course::getCredits)
                        .thenComparing(Course::getName))
                .collect(Collectors.toList());
        return sortedCourses;
    }

    /**
     * finds courses from the course repo where number of enrolled students is less than maximum enroll limit
     *
     * @return courses with free places
     * @throws SQLException if connection to database could not succeed
     */
    public List<Course> retrieveCoursesWithFreePlaces() throws SQLException {
        List<Course> freePlaces = coursesRepo.findAll()
                .stream()
                .filter(c -> c.getStudentsEnrolled().size() < c.getMaxEnrollment())
                .collect(Collectors.toList());

        return freePlaces;
    }

    /**
     * filters the Student's repository list by maximum credits number
     *
     * @return the filtered list
     */
    public List<Student> filterStudents() throws SQLException {
        List<Student> filteredStudents = this.studentsRepo.findAll()
                .stream()
                .filter(stud -> stud.getTotalCredits() == 30)
                .collect(Collectors.toList());
        return filteredStudents;
    }

    /**
     * filters the Course repository list by courses with more than 10 credits
     *
     * @return the filtered list
     */
    public List<Course> filterCourses() throws SQLException {
        return this.coursesRepo.findAll()
                .stream()
                .filter(course -> course.getCredits() > 10)
                .collect(Collectors.toList());
    }

    /**
     * desc: enroll a student to a course
     *
     * @param course  Course object
     * @param student Student object
     * @return true if successfully enrolled, else false
     * @throws InputException if course or student params not existing in repo lists
     *                        or if student can not enroll to that course
     * @throws NullException  if course or student ID's are null
     * @throws InputException if student can not enroll to the course
     * @throws SQLException   if connection to database could not succeed
     */
    public boolean register(Course course, Student student) throws InputException, SQLException, NullException {
        //checks if all data is correct
        if (course == null || coursesRepo.findOne(course.getCourseId()) == null) {
            throw new InputException("Non existing course id!");
        }

        if (student == null || studentsRepo.findOne(student.getStudentId()) == null) {
            throw new InputException("Non existing student id!");
        }
        List<Student> courseStudents = course.getStudentsEnrolled();

        if (courseStudents.size() == course.getMaxEnrollment()) {
            throw new InputException("Course has no free places!");
        }

        boolean found = courseStudents
                .stream()
                .anyMatch(s -> s.compareTo(student));

        if (found)
            throw new InputException("Student is already enrolled!");

        //if student has over 30 credits after enrolling to this course
        int studCredits = student.getTotalCredits() + course.getCredits();
        if (studCredits > 30)
            throw new InputException("Warning! Total number of credits exceeded!");

        // updating with the new data
        courseStudents.add(student);
        course.setStudentsEnrolled(courseStudents);
        coursesRepo.update(course);
        student.setTotalCredits(studCredits);
        List<Course> studCourses = student.getEnrolledCourses();
        studCourses.add(course);
        student.setEnrolledCourses(studCourses);
        studentsRepo.update(student);

        return true;
    }

    /**
     * deletes a course from a teacher. Removing course from the teacher's courses list, from the students' enrolled lists and from the course repository
     *
     * @param teacher Teacher object from whom we delete a course
     * @param course  Course object, from the teacher's list, to be deleted
     * @return true if successfully deleted
     * @throws InputException if teacher or course do not exist in te repo lists
     * @throws NullException  if course or id  is null
     * @throws IOException    if there occurs an error with the ObjectOutputStream in the update() or remove() method
     * @throws SQLException   if connection to database could not succeed
     */
    public boolean deleteCourseFromTeacher(Teacher teacher, Course course) throws InputException, NullException, SQLException {
        //checks if all data is correct
        if (coursesRepo.findOne(course.getCourseId()) == null) {
            throw new InputException("Non-existing course id!");
        }

        if (teachersRepo.findOne(teacher.getTeacherId()) == null) {
            throw new InputException("Non-existing teacher id!");
        }

        List<Course> courseList = teacher.getCourses();
        Optional<Course> c = courseList
                .stream()
                .filter(el -> el.compareTo(course))
                .findFirst();

        coursesRepo.delete(course.getCourseId());
        this.updateStudentsCredits();
        return true;
    }

    /**
     * recalculate the sum of credits and updates the credits sum for each student
     *
     * @throws SQLException if connection to database could not succeed
     */
    public void updateStudentsCredits() throws SQLException {
        this.getAllStudents()
                .forEach(s -> {
                    s.setTotalCredits(s.getEnrolledCourses()
                            .stream()
                            .mapToInt(Course::getCredits)
                            .reduce(0, Integer::sum));
                    try {
                        studentsRepo.update(s);
                    } catch (SQLException | NullException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * retrieve all students enrolled to a course
     *
     * @param course Course object
     * @return list of students enrolled to the given course, else null
     * @throws InputException if the course is null
     * @throws NullException  if the courseId is null
     * @throws SQLException   if connection to database could not succeed
     */
    public List<Student> retrieveStudentsEnrolledForACourse(Course course) throws InputException, SQLException, NullException {
        if (course == null) {
            throw new InputException("Non-existing course id!");
        }
        if (coursesRepo.findOne(course.getCourseId()) != null) {
            return course.getStudentsEnrolled();
        }

        return null;
    }

    /**
     * modifying credit number for a course
     *
     * @param c Course object, which credits were updated
     * @throws NullException if id of a course is null
     * @throws SQLException  if connection to database could not succeed
     */
    public void modifyCredits(Course c) throws NullException, SQLException {
        /* update course in the repo */
        this.coursesRepo.update(c);

        /* update all students */
        this.updateStudentsCredits();
    }

    /**
     * save course and teacher to its own repository
     *
     * @param c course to be added
     * @throws NullException if course id is null
     * @throws SQLException  if connection to database could not succeed
     */
    public boolean addCourse(Course c) throws SQLException, NullException {
        this.coursesRepo.save(c);
        return true;
    }

    /**
     * gets all students from the repository
     *
     * @return student list from the student repository
     * @throws SQLException if connection to database could not succeed
     */
    public List<Student> getAllStudents() throws SQLException {
        return this.studentsRepo.findAll();
    }

    /**
     * gets all courses from the repository
     *
     * @return courses list from the course repository
     * @throws SQLException if connection to database could not succeed
     */
    public List<Course> getAllCourses() throws SQLException {
        return this.coursesRepo.findAll();
    }

    /**
     * get all teachers from the repository
     *
     * @return teachers list from teh teacher repository
     * @throws SQLException if connection to database could not succeed
     */
    public List<Teacher> getAllTeachers() throws SQLException {
        return this.teachersRepo.findAll();
    }

    /**
     * searches for a student in the repository by the ID
     *
     * @param id of a Student object
     * @return Student object from the student repo list with the given ID
     * @throws NullException if student ID is null
     * @throws SQLException  if connection to database could not succeed
     */
    public Student findOneStudent(long id) throws SQLException, NullException {
        return this.studentsRepo.findOne(id);
    }

    /**
     * searches for a course in the repository by the ID
     *
     * @param id of a Course object
     * @return Course object from the course repository list with the given ID
     * @throws NullException if course ID is null
     * @throws SQLException  if connection to database could not succeed
     */
    public Course findOneCourse(long id) throws SQLException, NullException {
        return this.coursesRepo.findOne(id);
    }

    /**
     * searches for a teacher in the repository by the ID
     *
     * @param id of a Teacher object
     * @return Teacher object from the teacher repository list with the given ID
     * @throws NullException if teacher ID is null
     * @throws SQLException  if connection to database could not succeed
     */
    public Teacher findOneTeacher(long id) throws SQLException, NullException {
        return this.teachersRepo.findOne(id);
    }
}
