package com.app.schoolapp.domain;

import lombok.*;


import javax.persistence.*;
import java.util.*;

@Table(name = "courses")
@Getter
@Setter
@Entity(name = "courses")
@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Course{
    @Id
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String title;


    @ManyToMany(mappedBy = "courses")
    private Set<Student> attendedByStudentsIds = new HashSet<>();

    public void addStudent(Student student){
        attendedByStudentsIds.add(student);
        student.getCourses().add(this);
    }

    public void removeStudent(Student student){
        attendedByStudentsIds.remove(student);
        student.getCourses().remove(this);
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
        Course that = (Course) o;
        return Objects.equals(id, that.id);
    }
}
