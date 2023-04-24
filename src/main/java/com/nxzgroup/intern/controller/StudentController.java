package com.nxzgroup.intern.controller;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Sort.Direction;


import com.nxzgroup.intern.service.StudentService;
import com.nxzgroup.intern.model.Student;
@CrossOrigin
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    StudentService studentService;
/* 
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
*/
@GetMapping("/")
public ResponseEntity<Object> getAllStudents(
    @RequestParam(name = "page", required = false) Integer page,
    @RequestParam(name = "name", required = false) String name,
    @RequestParam(name = "lastname", required = false) String lastname,
    @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
    @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder
    ) {

    List<Student> students;
    int pageSize = 10;

    if (name != null && !name.isBlank() && lastname != null && !lastname.isBlank()) {
        students = studentService.retrieveStudentByNameAndLastName(name, lastname, Sort.by(sortOrder.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC, sortBy));
    } else if (name != null && !name.isBlank()) {
        students = studentService.retrieveStudentByName(name, Sort.by(sortOrder.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC, sortBy));
    } else if (lastname != null && !lastname.isBlank()) {
        students = studentService.retrieveStudentByLastName(lastname, Sort.by(sortOrder.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC, sortBy));
    } else {
        students = studentService.retrieveStudent();
    }

    if (students.isEmpty()) {
        return ResponseEntity.noContent().build();
    }

    int totalStudents = students.size();

    if(sortBy != null && !sortBy.isBlank() && (sortOrder.equalsIgnoreCase("asc") || sortOrder.equalsIgnoreCase("desc"))){
        if(sortOrder.equalsIgnoreCase("asc")){
            if(sortBy.equalsIgnoreCase("id")){
                students.sort(Comparator.comparing(Student::getId));
            }
        }else if(sortOrder.equalsIgnoreCase("desc")){
            if(sortBy.equalsIgnoreCase("id")){
                students.sort(Comparator.comparing(Student::getId).reversed());
            }
        }
    } else {
        return ResponseEntity.badRequest().build();
    }

    if (students.isEmpty()) {
        return ResponseEntity.noContent().build();
    }

    int totalPages = (int) Math.ceil((double) totalStudents / pageSize);

    if (name != null && !name.isBlank() && lastname != null && !lastname.isBlank()) {
        Map<String, Object> response = new HashMap<>();
        response.put("students", students.subList(0, Math.min(pageSize, totalStudents)));
        response.put("totalPages", totalPages);
        return ResponseEntity.ok(response);
    }

    if (page != null) {
        if (page < 1 || page > totalPages) {
            return ResponseEntity.badRequest().build();
        }
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalStudents);
        students = students.subList(startIndex, endIndex);

        Map<String, Object> response = new HashMap<>();
        response.put("students", students);
        response.put("totalPages", totalPages);
        return ResponseEntity.ok(response);
    } else {
        return ResponseEntity.ok(students.subList(0, Math.min(pageSize, totalStudents)));
    }
}
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        Optional<Student> student = studentService.retrieveStudent(id);
        if(!student.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping(params = "name")
    public List<Student> getStudent(@RequestParam(value = "name") String name) {
        return studentService.retrieveStudent(name);
    }

    //@PostMapping()
    @RequestMapping(value = "/", produces = "application/json", method=RequestMethod.POST)
    public ResponseEntity<?> postStudent(@RequestBody Student body) {
        Student student = studentService.createStudent(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> puStudent(@PathVariable Long id, @RequestBody Student body) {
        Optional<Student> student = studentService.updateStudent(id, body);
        if(!student.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", produces = "application/json", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        if(!studentService.deleteStudent(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}
