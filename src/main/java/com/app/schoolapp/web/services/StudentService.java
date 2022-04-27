package com.app.schoolapp.web.services;

import com.app.schoolapp.domain.Course;
import com.app.schoolapp.domain.Student;
import com.app.schoolapp.web.exceptions.NotFoundStudentException;
import com.app.schoolapp.web.repositories.CourseRepository;
import com.app.schoolapp.web.repositories.StudentRepository;
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
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public Student save(Student student, List<Integer> courseIDs) {
        courseRepository.findAllById(courseIDs)
                .forEach(x -> x.addStudent(student));
        return studentRepository.save(student);
    }

    public void register(int id, List<Integer> courseIDs) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isEmpty()) return;
        Student toSave = student.get();
        courseRepository.findAllById(courseIDs)
                .forEach(x -> x.addStudent(toSave));
        studentRepository.save(toSave);
    }

    public Optional<Student> getById(int id) {
        return studentRepository.findById(id);
    }

    public List<Course> findCoursesOfStudent(int id) {
        return studentRepository.findCoursesOfStudent(id);
    }

    public List<Student> findStudentsWithNoCourse() {
        return studentRepository.findStudentsWithNoCourse();
    }

    public List<Student> findStudents(List<SearchCriteria> criteria) {
        List<Student> mylist;
        Specification<Student> spec = null;
        StudentSpecificationBuilder builder = new StudentSpecificationBuilder();
        if (!criteria.isEmpty()) {
            criteria.forEach(builder::with);
            spec = builder.build();
        }
        if (spec != null) {
            mylist = studentRepository.findAll(spec);
        } else {
            mylist = studentRepository.findAll();
        }
        return mylist;
    }

    public void deleteById(Integer id) throws NotFoundStudentException {
        try {
            studentRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundStudentException("Failed to delete " + id + " student");
        }
    }

    public Student upsert(Student newStudent, int id) {
        return studentRepository.findById(id)
                .map(x -> {
                    x.setName(newStudent.getName());
                    x.setSurname(newStudent.getSurname());
                    return studentRepository.save(x);
                })
                .orElseGet(() -> studentRepository.save(newStudent));
    }
}


