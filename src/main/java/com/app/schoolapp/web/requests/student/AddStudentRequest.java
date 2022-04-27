package com.app.schoolapp.web.requests.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class AddStudentRequest {
    String name;
    String surname;

    @JsonProperty("courses_ids")
    List<Integer> courses;
}
