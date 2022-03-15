package com.phrontis.registration.service;

import com.phrontis.registration.controller.exception.CourseEnrolledException;
import com.phrontis.registration.controller.exception.NoRegistrationFoundException;
import com.phrontis.registration.controller.exception.ResourceNotFoundException;
import com.phrontis.registration.entity.CourseEntity;
import com.phrontis.registration.entity.StudentCourseEntity;
import com.phrontis.registration.entity.StudentCourseKey;
import com.phrontis.registration.entity.StudentEntity;
import com.phrontis.registration.repo.CourseRepository;
import com.phrontis.registration.repo.StudentCourseRepository;
import com.phrontis.registration.repo.StudentRepository;
import com.phrontis.registration.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    public Page<StudentEntity> getStudents(Integer pageNumber, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return studentRepository.findAll(pageable);
    }

    public void  addStudent(StudentEntity studentEntity){
        studentRepository.save(studentEntity);
    }

    public void updateStudent(Long studentId, StudentEntity studentEntity) throws ResourceNotFoundException {
        Optional<StudentEntity> studentOptional = studentRepository.findById(studentId);
        if (!studentOptional.isPresent()) {
            throw new ResourceNotFoundException();
        }
        studentEntity.setId(studentId);
        studentRepository.save(studentEntity);
    }

    public void removeStudent(Long studentId) throws ResourceNotFoundException {
        Optional<StudentEntity> studentOptional = studentRepository.findById(studentId);
        if (!studentOptional.isPresent()) {
            throw new ResourceNotFoundException();
        }
        studentRepository.deleteById(studentId);
    }

    public boolean enrollCourse(Long studentId, Long courseId) throws Exception {

        StudentCourseEntity studentCourseEntity = studentCourseRepository.getByStudentAndCourse(studentId, courseId);
        if (studentCourseEntity != null) {
            throw new CourseEnrolledException("Alread enrolled");
        }

        Optional<StudentEntity> studentOptional = studentRepository.findById(studentId);
        if (!studentOptional.isPresent()) {
            throw new NoRegistrationFoundException("Student not registered");
        }

        Optional<CourseEntity> courseOptional = courseRepository.findById(courseId);
        if (!courseOptional.isPresent()) {
            throw new NoRegistrationFoundException("Course not registered");
        }
        StudentEntity student = studentOptional.get();
        CourseEntity course = courseOptional.get();

        if (studentCourseRepository.getNoOfEnrolledCourses(studentId) >= Constant.MAX_ENROLLED_COURSE) {
            throw new CourseEnrolledException("Exceed enrolled course");
        }
        if (studentCourseRepository.getNoOfEnrolledStudent(courseId) >= Constant.MAX_REGISTERED_USER) {
            throw new CourseEnrolledException("Exceed enrolled student");
        }

        StudentCourseEntity studentCourse = new StudentCourseEntity();
        StudentCourseKey studentCourseKey = new StudentCourseKey();
        studentCourseKey.setCourseId(courseId);
        studentCourseKey.setStudentId(studentId);
        studentCourse.setId(studentCourseKey);
        studentCourse.setCourse(course);
        studentCourse.setStudent(student);
        studentCourseRepository.save(studentCourse);
        return true;
    }

    public List<CourseEntity> getEnrolledCourses(Long studentId) throws Exception {
        List<CourseEntity> courses = studentCourseRepository.getAllEnrolledCourses(studentId);

        return courses;
    }

    public List<StudentEntity> getEnrolledStudents(Long courseId) throws Exception {
        List<StudentEntity> students = studentCourseRepository.getAllEnrolledStudents(courseId);
        return students;
    }

    public List<StudentEntity> getStudentWithNocourse() throws Exception {
        List<Long> studentIds = studentCourseRepository.getStudentsWithNoCourse();
        List<StudentEntity> students = studentRepository.findAllById(studentIds);
        return students;
    }

    public List<CourseEntity> getCourseWithNoStudent() throws Exception {
        List<Long> courseIds = studentCourseRepository.getCourseWithNoStudent();
        List<CourseEntity> courses = courseRepository.findAllById(courseIds);
        return courses;
    }
}
