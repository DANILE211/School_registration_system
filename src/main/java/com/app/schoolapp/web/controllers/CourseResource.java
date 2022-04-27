package com.app.schoolapp.web.controllers;

import com.app.schoolapp.domain.Course;
import com.app.schoolapp.domain.Student;
import com.app.schoolapp.web.exceptions.NotFoundCourseException;
import com.app.schoolapp.web.mappers.course.*;
import com.app.schoolapp.web.mappers.student.GetStudentsResponseMapper;
import com.app.schoolapp.web.requests.course.AddCourseRequest;
import com.app.schoolapp.web.requests.course.UpsertCourseRequest;
import com.app.schoolapp.web.responses.course.AddCourseResponse;
import com.app.schoolapp.web.responses.course.GetCourseByIdResponse;
import com.app.schoolapp.web.responses.course.GetCoursesResponse;
import com.app.schoolapp.web.responses.course.UpsertCourseResponse;
import com.app.schoolapp.web.responses.student.GetStudentsResponse;
import com.app.schoolapp.web.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/courses")
@Tag(name = "Courses")
@RestController
@Slf4j
@AllArgsConstructor
public class CourseResource {

    private final CourseService courseService;
    private final AddCourseRequestMapper addCourseRequestMapper;
    private final AddCourseResponseMapper addCourseResponseMapper;
    private final GetCourseByIdResponseMapper getCourseByIdResponseMapper;
    private final GetStudentsResponseMapper getStudentsResponseMapper;
    private final GetCoursesRequestMapper getCoursesRequestMapper;
    private final GetCoursesResponseMapper getCoursesResponseMapper;
    private final UpsertCourseRequestMapper upsertCourseRequestMapperu;
    private final UpsertCourseResponseMapper upsertCourseResponseMapper;

    @Operation(description = "Adding Course")
    @ApiResponses(value = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = AddCourseResponse.class)),
                    description = "Course has been successfully added",
                    responseCode = "200")
    })
    @PostMapping(produces = "application/json")
    public ResponseEntity<AddCourseResponse> addCourse(@RequestBody AddCourseRequest request){
        return ResponseEntity
                .ok(addCourseResponseMapper
                        .map(courseService
                                .save(addCourseRequestMapper.map(request),request.getAttendees())));
    }

    @Operation(description = "Getting specific Course of given ID")
    @ApiResponses(value = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = GetCourseByIdResponse.class)) ,
                    description = "Adequate course is returned in the body",
                    responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })

    @GetMapping(value = "{id}", produces = "application/json")
    public ResponseEntity<GetCourseByIdResponse> getCourseById(
            @Parameter(description = "id of the requested Course") @PathVariable("id") int id){
        return courseService.getById(id)
                .map(course -> ResponseEntity.ok(getCourseByIdResponseMapper.map(course)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(description = "Getting all courses or filtered with parameters")
    @ApiResponses(value = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = GetCoursesResponse.class)),
                    description = "Resulting set of courses",
                    responseCode = "200")
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<GetCoursesResponse> search(
            @Parameter(description = "format:  \\<key\\> \\<operator\\> \\<value\\> ") @RequestParam(required = false) String search) {
        List<Course> courseList = courseService.findCourses(getCoursesRequestMapper.map(search));
        return ResponseEntity.ok(getCoursesResponseMapper.map(courseList));
    }

    @Operation(description = "Getting all students of specific course")
    @ApiResponses(value = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = GetStudentsResponse.class)),
                    description = "Resulting set of students",
                    responseCode = "200")
    })
    @GetMapping(value = "/students/{id}", produces = "application/json")
    public ResponseEntity<GetStudentsResponse> getStudentsOfSpecificCourse(
            @Parameter(description = "id of the requested course") @PathVariable("id") int id) {
        List<Student> studentList = courseService.findAttendeesOfCourse(id);
        return ResponseEntity.ok(getStudentsResponseMapper.map(studentList));
    }

    @Operation(description = "Getting courses not assigned to any student")
    @ApiResponses(value = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = GetCoursesResponse.class)),
                    description = "Resulting set of courses",
                    responseCode = "200")
    })

    @GetMapping(value = "/unallocated", produces = "application/json")
    public ResponseEntity<GetCoursesResponse> getCourseWithNoStudent() {
        List<Course> courseList = courseService.findCoursesWithNoStudent();
        return ResponseEntity.ok(getCoursesResponseMapper.map(courseList));
    }

    @Operation(description = "Deletes specific Course of given ID")
    @ApiResponses(value = {
            @ApiResponse(content = @Content, description = "Course has been successfully removed", responseCode = "204"),
            @ApiResponse(content = @Content, responseCode = "404", description = "Course not found")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCourse(
            @Parameter(description = "id of the Course to delete") @PathVariable("id") int id){
        try{
            courseService.deleteById(id);
        } catch (NotFoundCourseException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Updates specific Course. Creates one if not found")
    @ApiResponses(value = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = UpsertCourseResponse.class)),
                    description = "Body of the resulting course",
                    responseCode = "200")
    })
    @PutMapping(value = "{id}", produces = "application/json")
    ResponseEntity<UpsertCourseResponse> upsertCourse(@RequestBody UpsertCourseRequest request,
                                                        @Parameter(description = "id of the Course to update") @PathVariable int id) {
        return ResponseEntity.ok(upsertCourseResponseMapper
                .map(courseService.upsert(upsertCourseRequestMapperu.map(request), id)));
    }
}
