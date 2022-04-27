package com.app.schoolapp.web.services;

import com.app.schoolapp.domain.Course;
import com.app.schoolapp.domain.Student;
import com.app.schoolapp.web.exceptions.NotFoundCourseException;
import com.app.schoolapp.web.repositories.CourseRepository;
import com.app.schoolapp.web.repositories.StudentRepository;
import com.app.schoolapp.web.repositories.builders.CourseSpecificationBuilder;
import com.app.schoolapp.web.repositories.builders.StudentSpecificationBuilder;
import com.app.schoolapp.web.repositories.specifications.SearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public Course save(Course course, List<Integer> studentsIDs) {
        studentRepository.findAllById(studentsIDs)
                .forEach(x -> x.addCourse(course));
        return courseRepository.save(course);
    }

    public List<Student> findAttendeesOfCourse(int id){
        return courseRepository.findAttendeesOfCourse(id);
    }

    public Optional<Course> getById(int id) {
        return courseRepository.findById(id);
    }

    public List<Course> findCoursesWithNoStudent(){
        return courseRepository.findCoursesWithNoStudent();
    }

    public List<Course> findCourses(List<SearchCriteria> criteria){
        List<Course> mylist;
        Specification<Course> spec = null;
        CourseSpecificationBuilder builder = new CourseSpecificationBuilder();
        if(!criteria.isEmpty()){
            criteria.forEach(builder::with);
            spec = builder.build();
        }
        if(spec != null) {
            mylist = courseRepository.findAll(spec);
        }
        else {
            mylist = courseRepository.findAll();
        }
        return mylist;
    }

    public void deleteById(Integer id) throws NotFoundCourseException {
        try {
            courseRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundCourseException("Failed to delete " + id + " course" );
        }
    }

    public Course upsert(Course newCourse, int id) {
        return courseRepository.findById(id)
                .map(x -> {
                    x.setTitle(newCourse.getTitle());
                    return courseRepository.save(x);
                })
                .orElseGet(() -> {
                    return courseRepository.save(newCourse);
                });
    }
}