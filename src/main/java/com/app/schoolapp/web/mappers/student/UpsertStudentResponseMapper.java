package com.app.schoolapp.web.mappers.student;

import com.app.schoolapp.domain.Student;
import com.app.schoolapp.web.responses.student.StudentNoRecursion;
import com.app.schoolapp.web.responses.student.UpsertStudentResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
public class UpsertStudentResponseMapper {
    public UpsertStudentResponse map(Student student){
        return new UpsertStudentResponse(new StudentNoRecursion(student.getId(), student.getName(),
                student.getSurname()));
    }
}