package com.phrontis.registration.controller;

import com.phrontis.registration.controller.exception.CourseEnrolledException;
import com.phrontis.registration.controller.exception.NoRegistrationFoundException;
import com.phrontis.registration.controller.exception.ResourceNotFoundException;
import com.phrontis.registration.entity.CourseEntity;
import com.phrontis.registration.entity.StudentCourseKey;
import com.phrontis.registration.entity.StudentEntity;
import com.phrontis.registration.service.StudentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping
    public ResponseEntity getStudents(Integer pageNumber, Integer pageSize){
        try{
            Page<StudentEntity> studentEntities = studentService.getStudents(pageNumber, pageSize);
            if(!studentEntities.isEmpty()){
                return ResponseEntity.ok().body(studentEntities);
            } else{
                return  ResponseEntity.notFound().build();
            }
        } catch (Exception exception){
            return  ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public void addStudent(StudentEntity studentEntity){
        studentService.addStudent(studentEntity);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity updateStudentInfo(@PathVariable("studentId") Long studentId, StudentEntity studentEntity){
        try{
            studentService.updateStudent(studentId, studentEntity);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex){
            return  ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity deleteStudent(@PathVariable("studentId") Long studentId){
        try {
            studentService.removeStudent(studentId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex){
            return  ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "API for students to register to courses")
    @PostMapping("/enroll")
    public ResponseEntity enrollCourse(Long studentId, Long courseId) {
        try {
            studentService.enrollCourse(studentId, courseId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ex.getMessage());
        }
    }

    @ApiOperation(value = "Filter all courses with a specific student")
    @GetMapping("/enrolled_courses")
    public ResponseEntity getEnrolledCourses(Long studentId) {
        try {
            List<CourseEntity> courses = studentService.getEnrolledCourses(studentId);
            if (courses.size() <= 0) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok().body(courses);
            }
        } catch (CourseEnrolledException | NoRegistrationFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Filter all students with a specific course")
    @GetMapping("/enrolled_students")
    public ResponseEntity getEnrolledStudents(Long courseId) {
        try {
            List<StudentEntity> students = studentService.getEnrolledStudents(courseId);
            if (students.size() <= 0) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok().body(students);
            }
        } catch (CourseEnrolledException | NoRegistrationFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Filter all students with a no course")
    @GetMapping("/unenrolled_students")
    public ResponseEntity getStudentsWithNoCourse() throws Exception {
        try {
            List<StudentEntity> students = studentService.getStudentWithNocourse();
            if (students.size() <= 0) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok().body(students);
            }
        } catch (CourseEnrolledException | NoRegistrationFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Filter all course with a no student")
    @GetMapping("/unenrolled_courses")
    public ResponseEntity getCourseWithNoStudents() throws Exception {
        try {
            List<CourseEntity> courses = studentService.getCourseWithNoStudent();
            if (courses.size() <= 0) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok().body(courses);
            }
        } catch (CourseEnrolledException | NoRegistrationFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
