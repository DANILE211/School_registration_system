package com.app.schoolapp.web.mappers.student;

import com.app.schoolapp.domain.Student;
import com.app.schoolapp.web.requests.student.UpsertStudentRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
public class UpsertStudentRequestMapper {
    public Student map(UpsertStudentRequest request){
        Student student = new Student();
        student.setName(request.getName());
        student.setSurname(request.getSurname());
        return student;
    }
}