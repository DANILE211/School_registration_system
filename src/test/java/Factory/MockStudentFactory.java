package Factory;

import com.app.schoolapp.domain.Student;
import com.app.schoolapp.web.repositories.specifications.SearchCriteria;
import com.app.schoolapp.web.repositories.specifications.StudentSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MockStudentFactory {
    public static Student mockStudent(int idx){
        Student student = new Student();
        student.setId(idx);
        student.setName(idx +"mockname");
        student.setSurname(idx +"mocksurname");
        return student;
    }
    public static List<Student> mockListOfStudents(int n){
        List<Student> myList = new ArrayList<>();
        for(int i=0;i<n; i++){
            myList.add(mockStudent(i));
        }
        return myList;
    }
    public static Specification<Student> mockStudentSpecificationIndex(Integer id){
        SearchCriteria searchCriteria = new SearchCriteria("id", ":", id);
        StudentSpecification studentSpecification = new StudentSpecification(searchCriteria);
        return Specification.where(studentSpecification);
    }
}
