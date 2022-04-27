package com.app.schoolapp.domain;

import lombok.*;


import javax.persistence.*;
import java.util.*;

@Table(name = "students")
@Getter
@Setter
@Entity(name = "students")
@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Student{
    @Id
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String surname;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "students_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

    public void addCourse(Course course){
        courses.add(course);
        course.getAttendedByStudentsIds().add(this);
    }

    public void removeCourse(Course course){
        courses.remove(course);
        course.getAttendedByStudentsIds().remove(this);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student that = (Student) o;
        return Objects.equals(id, that.id);
    }
}
