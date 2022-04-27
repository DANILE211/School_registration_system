package com.app.schoolapp.web.mappers.course;

import com.app.schoolapp.domain.Course;
import com.app.schoolapp.web.responses.course.CourseNoRecursion;
import com.app.schoolapp.web.responses.course.GetCourseByIdResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
public class GetCourseByIdResponseMapper {
    public GetCourseByIdResponse map(Course course) {
        return new GetCourseByIdResponse(new CourseNoRecursion(course.getId(), course.getTitle()));
    }
}
