###### School Course Registration Service


How To Run:
- GOTO project root directory
- Run "docker-compose build"
- Then run "docker-compose up"

It should deploy your application and APIs are accessible at Swagger.

Swagger UI is enabled.
Swagger UI URL: http://localhost:6868/swagger-ui.html
Base URL: localhost:6868/
API docs URL: http://localhost:6868/v2/api-docs


Few API End-Points:</br>
Course CRUD:</br>
/api/course

    GET : List of all courses
    POST : Create new course

/api/course/{courseId}

    PUT: Update a course
    DELETE: Delete a course

/api/student 

    GET: List of all students
    POST: Create new student

/api/student/{studentId}

    PUT: Update a student
    DELETE: Delete a student

/api/student/enroll

    POST: API for students to register to courses

/api/student/enrolled_courses

    GET: Filter all courses with a specific student

/api/student/enrolled_students

    GET: Filter all students with a specific course

/api/student/unenrolled_courses
    
    GET: Filter all course with a no student

/api/student/unenrolled_students
    
    GET: Filter all students with a no course







    
    

