package com.app.schoolapp.web.repositories;


import com.app.schoolapp.domain.Course;
import com.app.schoolapp.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer>, JpaSpecificationExecutor<Student> {
    @Query(  value = "SELECT s.courses FROM students s WHERE s.id = :student_id")
    List<Course> findCoursesOfStudent(@Param("student_id") Integer studentId);
    @Query( value = "SELECT s FROM students s WHERE size(s.courses) = 0")
    List<Student> findStudentsWithNoCourse();
}