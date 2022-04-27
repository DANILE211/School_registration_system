package com.app.schoolapp.web.mappers.course;

import com.app.schoolapp.domain.Course;
import com.app.schoolapp.web.responses.course.CourseNoRecursion;
import com.app.schoolapp.web.responses.course.UpsertCourseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
public class UpsertCourseResponseMapper {
    public UpsertCourseResponse map(Course course){
        return new UpsertCourseResponse(new CourseNoRecursion(course.getId(), course.getTitle()));
    }
}
