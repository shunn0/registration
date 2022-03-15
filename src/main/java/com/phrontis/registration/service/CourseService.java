package com.phrontis.registration.service;

import com.phrontis.registration.controller.exception.ResourceNotFoundException;
import com.phrontis.registration.entity.CourseEntity;
import com.phrontis.registration.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    public Page<CourseEntity> getCourses(Integer pageNumber, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        return courseRepository.findAll(pageable);
    }

    public void addCourse(CourseEntity courseEntity){
        courseRepository.save(courseEntity);
    }

    public void updateCourse(Long studentId, CourseEntity studentEntity) throws ResourceNotFoundException {
        Optional<CourseEntity> studentOptional = courseRepository.findById(studentId);
        if (!studentOptional.isPresent()) {
            throw new ResourceNotFoundException();
        }
        studentEntity.setId(studentId);
        courseRepository.save(studentEntity);
    }

    public void removeCourse(Long studentId) throws ResourceNotFoundException {
        Optional<CourseEntity> studentOptional = courseRepository.findById(studentId);
        if (!studentOptional.isPresent()) {
            throw new ResourceNotFoundException();
        }
        courseRepository.deleteById(studentId);
    }

}
