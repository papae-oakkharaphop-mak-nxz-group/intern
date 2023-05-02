package com.nxzgroup.intern.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nxzgroup.intern.service.StudentService;
import com.nxzgroup.intern.model.Student;

@CrossOrigin
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    StudentService studentService;

    @GetMapping("/")
    public ResponseEntity<Object> getAllStudents(
            //req param => model ?
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "lastname", required = false) String lastname,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder
    ) {
        try {
            return ResponseEntity.ok(studentService.getAllStudents(pageSize,page, name, lastname, sortBy, sortOrder));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        try {
            return studentService.getStudent(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }
    @RequestMapping(value = "/", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<?> postStudent(@RequestBody Student body) {

        try {
            Student student = studentService.createStudent(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> puStudent(@PathVariable Long id, @RequestBody Student body) {

        try {
            Optional<Student> student = studentService.updateStudent(id, body);
            if (!student.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }


    }

    @RequestMapping(value = "/{id}", produces = "application/json", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            if (!studentService.deleteStudent(id)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }


    }

}
