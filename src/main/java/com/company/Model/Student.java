package com.company.Model;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private long studentId;
    private transient int totalCredits;
    private transient List<Course> enrolledCourses;

    public Student(long studentId, String firstName, String lastName, int totalCredits) {
        this.studentId = studentId;
        this.totalCredits = totalCredits;
        this.enrolledCourses = new ArrayList() {
        };
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentId == student.studentId;
    }


    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    /**
     * comparation based on id
     *
     * @param other, Student object to compare with
     * @return true if objects are equal, else false
     */
    public boolean compareTo(Student other) {
        return this.studentId == other.getStudentId();
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", studentId=" + studentId +
                ", totalCredits=" + totalCredits +
                '}';
    }
}
