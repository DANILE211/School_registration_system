package com.app.schoolapp.web.responses.course;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseNoRecursion {
    private Integer id;
    private String title;
}
