package com.app.schoolapp.web.mappers.student;

import com.app.schoolapp.domain.Student;
import com.app.schoolapp.web.responses.student.AddStudentResponse;
import com.app.schoolapp.web.responses.student.StudentNoRecursion;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
public class AddStudentResponseMapper {
    public AddStudentResponse map(Student student){
        return new AddStudentResponse(new StudentNoRecursion(student.getId(), student.getName(), student.getSurname()));
    }
}