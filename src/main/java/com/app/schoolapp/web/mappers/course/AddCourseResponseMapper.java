package com.app.schoolapp.web.mappers.course;

import com.app.schoolapp.domain.Course;
import com.app.schoolapp.web.responses.course.AddCourseResponse;
import com.app.schoolapp.web.responses.course.CourseNoRecursion;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
public class AddCourseResponseMapper {
    public AddCourseResponse map(Course course){
        return new AddCourseResponse(new CourseNoRecursion(course.getId(), course.getTitle()));
    }
}
