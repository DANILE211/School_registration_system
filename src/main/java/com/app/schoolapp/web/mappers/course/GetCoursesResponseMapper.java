package com.app.schoolapp.web.mappers.course;

import com.app.schoolapp.domain.Course;
import com.app.schoolapp.web.responses.course.CourseNoRecursion;
import com.app.schoolapp.web.responses.course.GetCoursesResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Component
public class GetCoursesResponseMapper {
    public GetCoursesResponse map(List<Course> mylist){
        return new GetCoursesResponse(mylist.stream().map(x ->
                new CourseNoRecursion(x.getId(), x.getTitle())).collect(Collectors.toList()));
    }
}
