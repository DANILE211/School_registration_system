package Factory;


import com.app.schoolapp.domain.Course;
import com.app.schoolapp.web.repositories.specifications.CourseSpecification;
import com.app.schoolapp.web.repositories.specifications.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MockCourseFactory {
    public static Course mockCourse(int idx){
        Course course = new Course();
        course.setId(idx);
        course.setTitle(idx +"mockTitle");
        return course;
    }
    public static List<Course> mockListOfCourses(int n){
        List<Course> myList = new ArrayList<>();
        for(int i=0;i<n; i++){
            myList.add(mockCourse(i));
        }
        return myList;
    }
    public static Specification<Course> mockCourseSpecificationIndex(Integer id){
        SearchCriteria searchCriteria = new SearchCriteria("id", ":", id);
        CourseSpecification courseSpecification = new CourseSpecification(searchCriteria);
        return Specification.where(courseSpecification);
    }
}
