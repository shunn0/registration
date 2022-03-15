package com.phrontis.registration.repo;

import com.phrontis.registration.entity.CourseEntity;
import com.phrontis.registration.entity.StudentCourseEntity;
import com.phrontis.registration.entity.StudentCourseKey;
import com.phrontis.registration.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourseEntity, StudentCourseKey> {

    @Query("select sc from StudentCourseEntity sc where sc.id.studentId=?1 and sc.id.courseId=?2")
    public StudentCourseEntity getByStudentAndCourse(Long studentId, Long courseId);

    @Query("select count(sc) from StudentCourseEntity  sc where sc.id.studentId=?1")
    public Long getNoOfEnrolledCourses(Long studentId);

    @Query("select sc.course from StudentCourseEntity  sc where sc.id.studentId=?1")
    public List<CourseEntity> getAllEnrolledCourses(Long studentId);

    @Query("select count(sc) from StudentCourseEntity sc where sc.id.courseId=?1")
    public Long getNoOfEnrolledStudent(Long courseId);

    @Query("select sc.student from StudentCourseEntity sc where  sc.id.courseId = ?1")
    public List<StudentEntity> getAllEnrolledStudents(Long courseId);

    @Query("select s.id from StudentEntity s left join StudentCourseEntity sc on sc.id.studentId = s.id where sc.id.studentId is null")
    public List<Long> getStudentsWithNoCourse();

    @Query("select c.id from  CourseEntity c left join StudentCourseEntity sc on sc.id.courseId = c.id where sc.id.courseId is null ")
    public List<Long> getCourseWithNoStudent();
}
