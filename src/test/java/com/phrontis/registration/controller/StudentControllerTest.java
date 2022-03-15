package com.phrontis.registration.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.phrontis.registration.controller.exception.CourseEnrolledException;
import com.phrontis.registration.controller.exception.ResourceNotFoundException;
import com.phrontis.registration.entity.CourseEntity;
import com.phrontis.registration.entity.StudentEntity;
import com.phrontis.registration.service.StudentService;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {StudentController.class})
@ExtendWith(SpringExtension.class)
class StudentControllerTest {
    @Autowired
    private StudentController studentController;

    @MockBean
    private StudentService studentService;

    @Test
    void testGetStudents() throws Exception {
        when(this.studentService.getStudents((Integer) any(), (Integer) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/student");
        MockHttpServletRequestBuilder paramResult = getResult.param("pageNumber", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("pageSize", String.valueOf(1));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetStudents3() throws Exception {
        when(this.studentService.getStudents((Integer) any(), (Integer) any())).thenReturn(null);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/student");
        MockHttpServletRequestBuilder paramResult = getResult.param("pageNumber", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("pageSize", String.valueOf(1));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testAddStudent() throws Exception {
        when(this.studentService.getStudents((Integer) any(), (Integer) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/student");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testEnrollCourse() throws Exception {
        when(this.studentService.enrollCourse((Long) any(), (Long) any())).thenReturn(true);
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/api/student/enroll");
        MockHttpServletRequestBuilder paramResult = postResult.param("courseId", String.valueOf(1L));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("studentId", String.valueOf(1L));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testEnrollCourse2() throws Exception {
        when(this.studentService.enrollCourse((Long) any(), (Long) any())).thenThrow(new Exception("?"));
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/api/student/enroll");
        MockHttpServletRequestBuilder paramResult = postResult.param("courseId", String.valueOf(1L));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("studentId", String.valueOf(1L));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(417))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("?"));
    }

    @Test
    void testGetEnrolledCourses() throws Exception {
        when(this.studentService.getEnrolledCourses((Long) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/student/enrolled_courses");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("studentId", String.valueOf(1L));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetEnrolledCourses2() throws Exception {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(123L);
        courseEntity.setName("?");

        ArrayList<CourseEntity> courseEntityList = new ArrayList<>();
        courseEntityList.add(courseEntity);
        when(this.studentService.getEnrolledCourses((Long) any())).thenReturn(courseEntityList);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/student/enrolled_courses");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("studentId", String.valueOf(1L));
        MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[{\"id\":123,\"name\":\"?\"}]"));
    }

    @Test
    void testGetEnrolledCourses3() throws Exception {
        when(this.studentService.getEnrolledCourses((Long) any())).thenThrow(new Exception("?"));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/student/enrolled_courses");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("studentId", String.valueOf(1L));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetEnrolledCourses4() throws Exception {
        when(this.studentService.getEnrolledCourses((Long) any()))
                .thenThrow(new CourseEnrolledException("An error occurred"));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/student/enrolled_courses");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("studentId", String.valueOf(1L));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("An error occurred"));
    }

    @Test
    void testGetEnrolledStudents() throws Exception {
        when(this.studentService.getEnrolledStudents((Long) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/student/enrolled_students");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("courseId", String.valueOf(1L));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetEnrolledStudents2() throws Exception {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(123L);
        studentEntity.setName("?");

        ArrayList<StudentEntity> studentEntityList = new ArrayList<>();
        studentEntityList.add(studentEntity);
        when(this.studentService.getEnrolledStudents((Long) any())).thenReturn(studentEntityList);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/student/enrolled_students");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("courseId", String.valueOf(1L));
        MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[{\"id\":123,\"name\":\"?\"}]"));
    }

    @Test
    void testGetEnrolledStudents3() throws Exception {
        when(this.studentService.getEnrolledStudents((Long) any())).thenThrow(new Exception("?"));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/student/enrolled_students");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("courseId", String.valueOf(1L));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetEnrolledStudents4() throws Exception {
        when(this.studentService.getEnrolledStudents((Long) any()))
                .thenThrow(new CourseEnrolledException("An error occurred"));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/student/enrolled_students");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("courseId", String.valueOf(1L));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("An error occurred"));
    }

    @Test
    void testAddStudent3() throws Exception {
        when(this.studentService.getStudents((Integer) any(), (Integer) any())).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/student");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testDeleteStudent() throws Exception {
        doNothing().when(this.studentService).removeStudent((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/student/{studentId}", 123L);
        MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteStudent2() throws Exception {
        doThrow(new ResourceNotFoundException("An error occurred")).when(this.studentService).removeStudent((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/student/{studentId}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testDeleteStudent3() throws Exception {
        doNothing().when(this.studentService).removeStudent((Long) any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/api/student/{studentId}", 123L);
        deleteResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(deleteResult)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetCourseWithNoStudents() throws Exception {
        when(this.studentService.getCourseWithNoStudent()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/student/unenrolled_courses");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetCourseWithNoStudents2() throws Exception {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(123L);
        courseEntity.setName("?");

        ArrayList<CourseEntity> courseEntityList = new ArrayList<>();
        courseEntityList.add(courseEntity);
        when(this.studentService.getCourseWithNoStudent()).thenReturn(courseEntityList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/student/unenrolled_courses");
        MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[{\"id\":123,\"name\":\"?\"}]"));
    }

    @Test
    void testGetCourseWithNoStudents3() throws Exception {
        when(this.studentService.getCourseWithNoStudent()).thenThrow(new Exception("?"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/student/unenrolled_courses");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetCourseWithNoStudents4() throws Exception {
        when(this.studentService.getCourseWithNoStudent()).thenThrow(new CourseEnrolledException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/student/unenrolled_courses");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("An error occurred"));
    }

    @Test
    void testGetStudentsWithNoCourse() throws Exception {
        when(this.studentService.getStudentWithNocourse()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/student/unenrolled_students");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetStudentsWithNoCourse2() throws Exception {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(123L);
        studentEntity.setName("?");

        ArrayList<StudentEntity> studentEntityList = new ArrayList<>();
        studentEntityList.add(studentEntity);
        when(this.studentService.getStudentWithNocourse()).thenReturn(studentEntityList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/student/unenrolled_students");
        MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[{\"id\":123,\"name\":\"?\"}]"));
    }

    @Test
    void testGetStudentsWithNoCourse3() throws Exception {
        when(this.studentService.getStudentWithNocourse()).thenThrow(new Exception("?"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/student/unenrolled_students");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetStudentsWithNoCourse4() throws Exception {
        when(this.studentService.getStudentWithNocourse()).thenThrow(new CourseEnrolledException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/student/unenrolled_students");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("An error occurred"));
    }

    @Test
    void testUpdateStudentInfo() throws Exception {
        doNothing().when(this.studentService)
                .updateStudent((Long) any(), (com.phrontis.registration.entity.StudentEntity) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/student/{studentId}", 123L);
        MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateStudentInfo2() throws Exception {
        doThrow(new ResourceNotFoundException("An error occurred")).when(this.studentService)
                .updateStudent((Long) any(), (com.phrontis.registration.entity.StudentEntity) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/student/{studentId}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testUpdateStudentInfo3() throws Exception {
        doNothing().when(this.studentService)
                .updateStudent((Long) any(), (com.phrontis.registration.entity.StudentEntity) any());
        MockHttpServletRequestBuilder putResult = MockMvcRequestBuilders.put("/api/student/{studentId}", 123L);
        putResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(this.studentController)
                .build()
                .perform(putResult)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

