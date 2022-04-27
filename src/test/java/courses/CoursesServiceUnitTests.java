package courses;


import Factory.MockCourseFactory;
import Factory.MockStudentFactory;
import com.app.schoolapp.domain.Course;
import com.app.schoolapp.domain.Student;
import com.app.schoolapp.web.exceptions.NotFoundCourseException;
import com.app.schoolapp.web.repositories.CourseRepository;
import com.app.schoolapp.web.repositories.StudentRepository;
import com.app.schoolapp.web.repositories.specifications.SearchCriteria;
import com.app.schoolapp.web.services.CourseService;
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
public class CoursesServiceUnitTests {


    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Test
    public void getById_whenCourseExists() {
        Course course = MockCourseFactory.mockCourse(1);

        Mockito.when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        Optional<Course> result = courseService.getById(1);

        Assertions.assertThat(result).hasValue(course);
    }

    @Test
    public void getById_whenCourseDontExists() {
        Mockito.when(courseRepository.findById(2)).thenReturn(Optional.empty());

        Optional<Course> result = courseService.getById(2);

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void deleteById_whenIndexNotFound() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(courseRepository).deleteById(1);
        Assertions.assertThatExceptionOfType(NotFoundCourseException.class).isThrownBy(() -> courseService.deleteById(1));
    }

    @Test
    public void deleteById_whenIndexFound() {
        courseService.deleteById(2);
        Mockito.verify(courseRepository).deleteById(2);
    }

    @Test
    public void findAtendeesOfCourse_whenCourseHasAny() {
        List<Student> sourceList = MockStudentFactory.mockListOfStudents(5);

        Mockito.when(courseRepository.findAttendeesOfCourse(1)).thenReturn(sourceList);

        List<Student> resultList = courseService.findAttendeesOfCourse(1);

        Assertions.assertThat(resultList).containsExactlyElementsOf(sourceList);
    }

    @Test
    public void findCourseWithNoAtendees_whenSuchExists() {
        List<Course> sourceList = MockCourseFactory.mockListOfCourses(5);

        Mockito.when(courseRepository.findCoursesWithNoStudent()).thenReturn(sourceList);

        List<Course> resultList = courseService.findCoursesWithNoStudent();

        Assertions.assertThat(resultList).containsExactlyElementsOf(sourceList);
    }


    @Test
    public void save_whenNoAtendeesIndexesProvided() {
        Course course = MockCourseFactory.mockCourse(1);

        Mockito.when(courseRepository.save(course)).thenReturn(course);
        Mockito.when(studentRepository.findAllById(Collections.emptyList())).thenReturn(Collections.emptyList());

        Course result = courseService.save(course, Collections.emptyList());

        Assertions.assertThat(result).isEqualTo(course);
    }

    @Test
    public void save_whenAtendeesIndexesProvided() {
        Course course = MockCourseFactory.mockCourse(1);
        List<Integer> indexes = Arrays.asList(1, 2, 3);
        List<Student> sourceList = MockStudentFactory.mockListOfStudents(3);

        Mockito.when(courseRepository.save(course)).thenReturn(course);
        Mockito.when(studentRepository.findAllById(indexes)).thenReturn(sourceList);

        Course result = courseService.save(course, indexes);

        Assertions.assertThat(result).isEqualTo(course);
        Assertions.assertThat(sourceList).extracting(Student::getCourses).contains(Set.of(course));
    }

    @Test
    public void update_whenCourseAlreadyExists() {
        Course oldCourse = MockCourseFactory.mockCourse(1);
        Course newCourse = MockCourseFactory.mockCourse(2);
        Course correct = MockCourseFactory.mockCourse(2);
        correct.setId(1);

        Mockito.when(courseRepository.findById(1)).thenReturn(Optional.of(oldCourse));
        Mockito.when(courseRepository.save(newCourse)).thenReturn(correct);

        Course result = courseService.upsert(newCourse, 1);

        Assertions.assertThat(result).isEqualTo(correct);
    }

    @Test
    public void update_whenCourseDontExists() {
        Course newCourse = MockCourseFactory.mockCourse(2);
        Course correct = MockCourseFactory.mockCourse(2);
        correct.setId(1);

        Mockito.when(courseRepository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(courseRepository.save(newCourse)).thenReturn(correct);

        Course result = courseService.upsert(newCourse, 1);

        Assertions.assertThat(result).isEqualTo(correct);
    }

    @Test
    public void search_whenNoCriteria() {
        List<Course> myList = MockCourseFactory.mockListOfCourses(6);

        Mockito.when(courseRepository.findAll()).thenReturn(myList);

        List<Course> resultList = courseService.findCourses(Collections.emptyList());

        Assertions.assertThat(resultList).isEqualTo(myList);
    }

    @Test
    public void search_whenCriteriaProvided() {
        List<Course> correct = MockCourseFactory.mockListOfCourses(1);
        List<SearchCriteria> criteria = new ArrayList<>();
        criteria.add(new SearchCriteria("id", ":", 1));

        Specification<Course> spec = MockCourseFactory.mockCourseSpecificationIndex(1);
        Mockito.when(courseRepository.findAll(spec)).thenReturn(correct);

        List<Course> resultList = courseService.findCourses(criteria);

        Assertions.assertThat(resultList).isEqualTo(correct);
    }
}
