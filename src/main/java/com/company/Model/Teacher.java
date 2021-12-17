package com.company.Model;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person {
    private long teacherId;
    private transient List<Course> courses;

    public Teacher(long teacherId, String firstName, String lastName) {
        this.teacherId = teacherId;
        this.courses = new ArrayList() {
        };
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", teacherId=" + teacherId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return teacherId == teacher.teacherId;
    }


}
