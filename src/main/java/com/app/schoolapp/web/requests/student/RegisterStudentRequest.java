package com.app.schoolapp.web.requests.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisterStudentRequest {
    List<Integer> courses;
}
