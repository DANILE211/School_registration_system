package com.app.schoolapp.web.mappers.course;

import com.app.schoolapp.domain.Course;
import com.app.schoolapp.web.requests.course.UpsertCourseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
public class UpsertCourseRequestMapper {
    public Course map(UpsertCourseRequest request){
        Course course = new Course();
        course.setTitle(request.getTitle());
        return course;
    }
}
