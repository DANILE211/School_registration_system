package students;


import Factory.MockCourseFactory;
import Factory.MockStudentFactory;
import com.app.schoolapp.domain.Course;
import com.app.schoolapp.domain.Student;
import com.app.schoolapp.web.exceptions.NotFoundStudentException;
import com.app.schoolapp.web.repositories.CourseRepository;
import com.app.schoolapp.web.repositories.StudentRepository;
import com.app.schoolapp.web.repositories.specifications.SearchCriteria;
import com.app.schoolapp.web.services.StudentService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceUnitTests {


    @InjectMocks
    private StudentService studentService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Test
    public void getById_whenStudentExists() {
        Student student = MockStudentFactory.mockStudent(1);

        Mockito.when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.getById(1);

        Assertions.assertThat(result).hasValue(student);
    }

    @Test
    public void getById_whenStudentDontExists() {
        Mockito.when(studentRepository.findById(2)).thenReturn(Optional.empty());

        Optional<Student> result = studentService.getById(2);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void deleteById_whenIndexNotFound() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(studentRepository).deleteById(1);
        Assertions.assertThatExceptionOfType(NotFoundStudentException.class).isThrownBy(() -> studentService.deleteById(1));
    }

    @Test
    public void deleteById_whenIndexFound() {
        studentService.deleteById(2);
        Mockito.verify(studentRepository).deleteById(2);
    }

    @Test
    public void findCoursesOfStudent_whenStudentHasAny() {
        List<Course> sourceList = MockCourseFactory.mockListOfCourses(5);

        Mockito.when(studentRepository.findCoursesOfStudent(1)).thenReturn(sourceList);

        List<Course> resultList = studentService.findCoursesOfStudent(1);

        Assertions.assertThat(resultList).containsExactlyElementsOf(sourceList);
    }

    @Test
    public void findStudentsWithNoCourses_whenSuchExists() {
        List<Student> sourceList = MockStudentFactory.mockListOfStudents(5);

        Mockito.when(studentRepository.findStudentsWithNoCourse()).thenReturn(sourceList);

        List<Student> resultList = studentService.findStudentsWithNoCourse();

        Assertions.assertThat(resultList).containsExactlyElementsOf(sourceList);
    }

    @Test
    public void registerStudent_whenStudentFound() {
        Student student = MockStudentFactory.mockStudent(1);
        List<Course> courseList = MockCourseFactory.mockListOfCourses(5);

        Mockito.when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        Mockito.when(courseRepository.findAllById(Arrays.asList(1, 2, 3, 4, 5))).thenReturn(courseList);

        studentService.register(1, Arrays.asList(1, 2, 3, 4, 5));
        Mockito.verify(studentRepository).save(student);

        Assertions.assertThat(courseList).extracting(Course::getAttendedByStudentsIds).contains(Set.of(student));
        Assertions.assertThat(student.getCourses()).containsExactlyElementsOf(courseList);
    }

    @Test
    public void registerStudent_whenStudentNotFound() {
        List<Course> courseList = MockCourseFactory.mockListOfCourses(5);

        Mockito.when(studentRepository.findById(1)).thenReturn(Optional.empty());

        studentService.register(1, Arrays.asList(1, 2, 3, 4, 5));

        Mockito.verifyNoMoreInteractions(courseRepository);
    }

    @Test
    public void save_whenNoCourseIndexesProvided() {
        Student student = MockStudentFactory.mockStudent(1);

        Mockito.when(studentRepository.save(student)).thenReturn(student);
        Mockito.when(courseRepository.findAllById(Collections.emptyList())).thenReturn(Collections.emptyList());

        Student result = studentService.save(student, Collections.emptyList());

        Assertions.assertThat(result).isEqualTo(student);
    }

    @Test
    public void save_whenCourseIndexesProvided() {
        Student student = MockStudentFactory.mockStudent(1);
        List<Integer> indexes = Arrays.asList(1, 2, 3);
        List<Course> sourceList = MockCourseFactory.mockListOfCourses(3);

        Mockito.when(studentRepository.save(student)).thenReturn(student);
        Mockito.when(courseRepository.findAllById(indexes)).thenReturn(sourceList);

        Student result = studentService.save(student, indexes);

        Assertions.assertThat(result).isEqualTo(student);
        Assertions.assertThat(sourceList).extracting(Course::getAttendedByStudentsIds).contains(Set.of(student));
    }

    @Test
    public void update_whenStudentAlreadyExists() {
        Student oldStudent = MockStudentFactory.mockStudent(1);
        Student newStudent = MockStudentFactory.mockStudent(2);
        Student correct = MockStudentFactory.mockStudent(2);
        correct.setId(1);

        Mockito.when(studentRepository.findById(1)).thenReturn(Optional.of(oldStudent));
        Mockito.when(studentRepository.save(newStudent)).thenReturn(correct);

        Student result = studentService.upsert(newStudent, 1);

        Assertions.assertThat(result).isEqualTo(correct);
    }

    @Test
    public void update_whenStudentDontExists() {
        Student newStudent = MockStudentFactory.mockStudent(-1);
        Student correct = MockStudentFactory.mockStudent(2);
        correct.setId(1);

        Mockito.when(studentRepository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(studentRepository.save(newStudent)).thenReturn(correct);

        Student result = studentService.upsert(newStudent, 1);

        Assertions.assertThat(result).isEqualTo(correct);
    }

    @Test
    public void search_whenNoCriteria() {
        List<Student> myList = MockStudentFactory.mockListOfStudents(6);

        Mockito.when(studentRepository.findAll()).thenReturn(myList);

        List<Student> resultList = studentService.findStudents(Collections.emptyList());

        Assertions.assertThat(resultList).isEqualTo(myList);
    }

    @Test
    public void search_whenCriteriaProvided() {
        List<Student> correct = MockStudentFactory.mockListOfStudents(1);
        List<SearchCriteria> criteria = new ArrayList<>();
        criteria.add(new SearchCriteria("id", ":", 1));

        Specification<Student> spec = MockStudentFactory.mockStudentSpecificationIndex(1);
        Mockito.when(studentRepository.findAll(spec)).thenReturn(correct);

        List<Student> resultList = studentService.findStudents(criteria);

        Assertions.assertThat(resultList).isEqualTo(correct);
    }
}
