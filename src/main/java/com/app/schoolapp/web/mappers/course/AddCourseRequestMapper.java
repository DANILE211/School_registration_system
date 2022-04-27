package com.app.schoolapp.web.mappers.course;

import com.app.schoolapp.domain.Course;
import com.app.schoolapp.web.requests.course.AddCourseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
public class AddCourseRequestMapper {
    public Course map(AddCourseRequest request) {
        Course course = new Course();
        course.setTitle(request.getTitle());
        return course;
    }
}
