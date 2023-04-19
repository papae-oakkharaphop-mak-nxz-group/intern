package com.nxzgroup.intern.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nxzgroup.intern.service.StudentService;
import com.nxzgroup.intern.model.Student;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    StudentService studentService;

    @GetMapping("/")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.retrieveStudent();
        if(students.isEmpty()) {
        return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getStudentsByPage(@RequestParam(defaultValue = "1") int page) {
        int pageSize = 10; // Number of students to return per page
        List<Student> students = studentService.retrieveStudent();
        int totalStudents = students.size();
        int totalPages = (int) Math.ceil((double) totalStudents / pageSize);
        if (page >= totalPages) {
            return ResponseEntity.notFound().build();
        }
        int startIndex = page * pageSize - 10;
        int endIndex = Math.min(startIndex + pageSize, totalStudents);
        List<Student> pageStudents = students.subList(startIndex, endIndex);
        if (pageStudents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("students", pageStudents);
        response.put("totalPages", totalPages);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        Optional<Student> customer = studentService.retrieveStudent(id);
        if(!customer.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping(params = "name")
    public List<Student> getStudent(@RequestParam(value = "name") String name) {
        return studentService.retrieveStudent(name);
    }

    @PostMapping()
    public ResponseEntity<?> postStudent(@RequestBody Student body) {
        Student customer = studentService.createStudent(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> puStudent(@PathVariable Long id, @RequestBody Student body) {
        Optional<Student> customer = studentService.updateStudent(id, body);
        if(!customer.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        if(!studentService.deleteStudent(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}
