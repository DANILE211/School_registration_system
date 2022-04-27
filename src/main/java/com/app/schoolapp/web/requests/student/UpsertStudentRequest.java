package com.app.schoolapp.web.requests.student;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpsertStudentRequest{
    String name;
    String surname;
}
