package com.app.schoolapp.web.repositories;

import com.app.schoolapp.domain.Course;
import com.app.schoolapp.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Integer>, JpaSpecificationExecutor<Course> {
    @Query(  value = "SELECT t.attendedByStudentsIds FROM courses t WHERE t.id = :course_id")
    List<Student> findAttendeesOfCourse(@Param("course_id") Integer courseId);
    @Query( value = "SELECT c FROM courses c WHERE size(c.attendedByStudentsIds) = 0")
    List<Course> findCoursesWithNoStudent();
}
