package com.app.schoolapp.web.controllers;

import com.app.schoolapp.domain.Course;
import com.app.schoolapp.domain.Student;
import com.app.schoolapp.web.exceptions.NotFoundStudentException;
import com.app.schoolapp.web.mappers.course.GetCoursesResponseMapper;
import com.app.schoolapp.web.mappers.student.*;
import com.app.schoolapp.web.requests.student.AddStudentRequest;
import com.app.schoolapp.web.requests.student.RegisterStudentRequest;
import com.app.schoolapp.web.requests.student.UpsertStudentRequest;
import com.app.schoolapp.web.responses.course.GetCoursesResponse;
import com.app.schoolapp.web.responses.student.AddStudentResponse;
import com.app.schoolapp.web.responses.student.GetStudentByIdResponse;
import com.app.schoolapp.web.responses.student.GetStudentsResponse;
import com.app.schoolapp.web.responses.student.UpsertStudentResponse;
import com.app.schoolapp.web.services.StudentService;
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

@RequestMapping(value = "/students")
@Tag(name = "Students")
@RestController
@Slf4j
@AllArgsConstructor
public class StudentResource {

    private final StudentService studentService;
    private final AddStudentRequestMapper addStudentRequestMapper;
    private final AddStudentResponseMapper addStudentResponseMapper;
    private final GetStudentByIdResponseMapper getStudentByIdResponseMapper;
    private final GetStudentsResponseMapper getStudentsResponseMapper;
    private final GetStudentsRequestMapper getStudentsRequestMapper;
    private final GetCoursesResponseMapper getCoursesResponseMapper;
    private final UpsertStudentRequestMapper upsertStudentRequestMapper;
    private final UpsertStudentResponseMapper upsertStudentResponseMapper;

    @Operation(description = "Adding student")
    @ApiResponses(value = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = AddStudentResponse.class)),
                    description = "Student has been successfully added",
                    responseCode = "200")
    })
    @PostMapping(produces = "application/json")
    public ResponseEntity<AddStudentResponse> addStudent(@RequestBody AddStudentRequest request){
        return ResponseEntity
                .ok(addStudentResponseMapper
                        .map(studentService
                                .save(addStudentRequestMapper.map(request), request.getCourses())));
    }

    @Operation(description = "Register student to the course of given id")
    @ApiResponses(value = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = AddStudentResponse.class)),
                    description = "Student has been successfully registered",
                    responseCode = "200")
    })
    @PostMapping(value = "/register/{id}", produces = "application/json")
    public ResponseEntity<Void> registerStudentToCourse(@Parameter(description = "id of the requested student") @PathVariable("id") int id,
                                                        @RequestBody RegisterStudentRequest request){
        studentService.register(id, request.getCourses());
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Getting specific student of given ID")
    @ApiResponses(value = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = GetStudentByIdResponse.class)) ,
                    description = "Adequate student is returned in the body",
                    responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content)
    })

    @GetMapping(value = "{id}", produces = "application/json")
    public ResponseEntity<GetStudentByIdResponse> getStudentById(
            @Parameter(description = "id of the requested student") @PathVariable("id") int id){
        return studentService.getById(id)
                .map(student -> ResponseEntity.ok(getStudentByIdResponseMapper.map(student)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(description = "Getting all students or filtered with parameters")
    @ApiResponses(value = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = GetStudentsResponse.class)),
                    description = "Resulting set of courses",
                    responseCode = "200")
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<GetStudentsResponse> search(
            @Parameter(description = "format:  \\<key\\> \\<operator\\> \\<value\\> ") @RequestParam(required = false) String search) {
        List<Student> studentList = studentService.findStudents(getStudentsRequestMapper.map(search));
        return ResponseEntity.ok(getStudentsResponseMapper.map(studentList));
    }

    @Operation(description = "Getting all courses of specific student")
    @ApiResponses(value = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = GetCoursesResponse.class)),
                    description = "Resulting set of courses",
                    responseCode = "200")
    })
    @GetMapping(value = "/courses/{id}", produces = "application/json")
    public ResponseEntity<GetCoursesResponse> getCoursesOfSpecificStudent(
            @Parameter(description = "id of the requested student") @PathVariable("id") int id) {
        List<Course> courseList = studentService.findCoursesOfStudent(id);
        return ResponseEntity.ok(getCoursesResponseMapper.map(courseList));
    }

    @Operation(description = "Getting students not assigned to any course")
    @ApiResponses(value = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = GetStudentsResponse.class)),
                    description = "Resulting set of students",
                    responseCode = "200")
    })

    @GetMapping(value = "/unallocated", produces = "application/json")
    public ResponseEntity<GetStudentsResponse> getStudentWithNoCourse() {
        List<Student> studentList = studentService.findStudentsWithNoCourse();
        return ResponseEntity.ok(getStudentsResponseMapper.map(studentList));
    }

    @Operation(description = "Deletes specific student of given ID")
    @ApiResponses(value = {
            @ApiResponse(content = @Content, description = "Student has been successfully removed", responseCode = "204"),
            @ApiResponse(content = @Content, responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "id of the student to delete") @PathVariable("id") int id){
        try{
            studentService.deleteById(id);
        } catch (NotFoundStudentException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Updates specific student. Creates one if not found")
    @ApiResponses(value = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = UpsertStudentResponse.class)),
                    description = "Body of the resulting student",
                    responseCode = "200")
    })
    @PutMapping(value = "{id}", produces = "application/json")
    ResponseEntity<UpsertStudentResponse> upsertStudent(@RequestBody UpsertStudentRequest request,
                                                        @Parameter(description = "id of the student to update") @PathVariable int id) {
        return ResponseEntity.ok(upsertStudentResponseMapper
                .map(studentService.upsert(upsertStudentRequestMapper.map(request), id)));
    }
}
