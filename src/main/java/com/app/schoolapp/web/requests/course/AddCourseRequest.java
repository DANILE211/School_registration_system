package com.app.schoolapp.web.requests.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class AddCourseRequest {
    String title;

    @JsonProperty("students_ids")
    List<Integer> attendees;
}