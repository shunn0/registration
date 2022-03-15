package com.phrontis.registration.controller;

import com.phrontis.registration.controller.exception.ResourceNotFoundException;
import com.phrontis.registration.entity.CourseEntity;
import com.phrontis.registration.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    @GetMapping
    public ResponseEntity getCourseList(Integer pageNumber, Integer pageSize){
        try{
            Page<CourseEntity> courseEntities = courseService.getCourses(pageNumber,pageSize);
            if(!courseEntities.isEmpty()){
                return ResponseEntity.ok().body(courseEntities);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public void addCourse(CourseEntity courseEntity){
        courseService.addCourse(courseEntity);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity updateCourse(@PathVariable("courseId") Long courseId, @RequestBody CourseEntity courseEntity) {
        try{
            courseService.updateCourse(courseId, courseEntity);
            return ResponseEntity.ok().build();
        }
        catch(ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ex.getMessage());
        }
    }
    @DeleteMapping("/{courseId}")
    public ResponseEntity deleteCourse(@PathVariable("courseId") Long courseId) {
        try{
            courseService.removeCourse(courseId);
            return ResponseEntity.ok().build();
        }
        catch(ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ex.getMessage());
        }
    }
}
