package com.phrontis.registration.controller.exception;

public class CourseEnrolledException extends Exception{
    public CourseEnrolledException(String message){
        super(message);
    }

    public CourseEnrolledException(){
        super("Course Enrolled Exception.");
    }
}
