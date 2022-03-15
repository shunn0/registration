package com.phrontis.registration.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phrontis.registration.controller.exception.ResourceNotFoundException;
import com.phrontis.registration.entity.CourseEntity;
import com.phrontis.registration.service.CourseService;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {CourseController.class})
@ExtendWith(SpringExtension.class)
class CourseControllerTest {
    @Autowired
    private CourseController courseController;

    @MockBean
    private CourseService courseService;

    @Test
    void testAddCourse() throws Exception {
        when(this.courseService.getCourses((Integer) any(), (Integer) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/course");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.courseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testUpdateCourse() throws Exception {
        doNothing().when(this.courseService).updateCourse((Long) any(), (CourseEntity) any());

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(123L);
        courseEntity.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(courseEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/course/{courseId}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.courseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testUpdateCourse2() throws Exception {
        doThrow(new ResourceNotFoundException("An error occurred")).when(this.courseService)
                .updateCourse((Long) any(), (CourseEntity) any());

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setId(123L);
        courseEntity.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(courseEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/course/{courseId}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.courseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(417))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("An error occurred"));
    }

    @Test
    void testAddCourse3() throws Exception {
        when(this.courseService.getCourses((Integer) any(), (Integer) any())).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/course");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.courseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testDeleteCourse() throws Exception {
        doNothing().when(this.courseService).removeCourse((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/course/{courseId}", 123L);
        MockMvcBuilders.standaloneSetup(this.courseController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteCourse2() throws Exception {
        doThrow(new ResourceNotFoundException("An error occurred")).when(this.courseService).removeCourse((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/course/{courseId}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.courseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(417))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("An error occurred"));
    }

    @Test
    void testDeleteCourse3() throws Exception {
        doNothing().when(this.courseService).removeCourse((Long) any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/api/course/{courseId}", 123L);
        deleteResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(this.courseController)
                .build()
                .perform(deleteResult)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetCourseList() throws Exception {
        when(this.courseService.getCourses((Integer) any(), (Integer) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/course");
        MockHttpServletRequestBuilder paramResult = getResult.param("pageNumber", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("pageSize", String.valueOf(1));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.courseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetCourseList3() throws Exception {
        when(this.courseService.getCourses((Integer) any(), (Integer) any())).thenReturn(null);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/course");
        MockHttpServletRequestBuilder paramResult = getResult.param("pageNumber", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("pageSize", String.valueOf(1));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.courseController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

