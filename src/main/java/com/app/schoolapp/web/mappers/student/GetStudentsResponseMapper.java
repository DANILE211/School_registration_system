package com.app.schoolapp.web.mappers.student;

import com.app.schoolapp.domain.Student;
import com.app.schoolapp.web.responses.student.GetStudentsResponse;
import com.app.schoolapp.web.responses.student.StudentNoRecursion;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;
@Data
@AllArgsConstructor
@Component
public class GetStudentsResponseMapper {
    public GetStudentsResponse map(List<Student> mylist){
        return new GetStudentsResponse(mylist.stream().map(x ->
                new StudentNoRecursion(x.getId(), x.getName(), x.getSurname())).collect(Collectors.toList()));
    }
}
