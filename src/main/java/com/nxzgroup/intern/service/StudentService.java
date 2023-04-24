package com.nxzgroup.intern.service;

import java.util.*;

import com.nxzgroup.intern.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nxzgroup.intern.repository.StudentRepository;
import com.nxzgroup.intern.model.Student;

@Service
public class StudentService {
    private StudentRepository studentRepository;

    enum StudentFilter {
        BY_NAME_AND_LASTNAME,
        BY_NAME,
        BY_LASTNAME,
        ALL;

        public static StudentFilter fromValues(String name, String lastname) {
            if (name != null && !name.isBlank() && lastname != null && !lastname.isBlank()) {
                return BY_NAME_AND_LASTNAME;
            } else if (name != null && !name.isBlank()) {
                return BY_NAME;
            } else if (lastname != null && !lastname.isBlank()) {
                return BY_LASTNAME;
            } else {
                return ALL;
            }
        }
    }

    public List<Student> sortStudents(List<Student> students, String sortBy, String sortOrder) {
        if (sortBy != null && !sortBy.isBlank() && ("asc".equalsIgnoreCase(sortOrder) || "desc".equalsIgnoreCase(sortOrder))) {
            Comparator<Student> comparator = Comparator.comparing(Student::getId);
            if ("desc".equalsIgnoreCase(sortOrder)) {
                comparator = comparator.reversed();
            }
            students.sort(comparator);
            return students;
        } else {
            return null;
        }
    }

    public ResponseEntity<Object> getStudent(Long id){
        Optional<Student> student = retrieveStudent(id);
        if (!student.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }
    public ResponseEntity<Object> getAllStudents(Integer page,String name,String lastname,String sortBy,String sortOrder){
        List<Student> students = null;
        int pageSize = 10;

        StudentFilter filter = StudentFilter.fromValues(name, lastname);
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        switch (filter) {
            case BY_NAME_AND_LASTNAME:
                students = retrieveStudentByNameAndLastName(name, lastname, sort);
                break;
            case BY_NAME:
                students = retrieveStudentByName(name, sort);
                break;
            case BY_LASTNAME:
                students = retrieveStudentByLastName(lastname, sort);
                break;
            case ALL:
                students = retrieveStudent();
                break;
        }

        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        int totalStudents = students.size();

        ///
        if(sortBy != null && !sortBy.isBlank() && (sortOrder.equalsIgnoreCase("asc") || sortOrder.equalsIgnoreCase("desc"))){
            if(sortOrder.equalsIgnoreCase("asc")){
                    students.sort(Comparator.comparing(Student::getId));
            }
            if(sortOrder.equalsIgnoreCase("desc")){
                    students.sort(Comparator.comparing(Student::getId).reversed());
            }
        } else {
            return ResponseEntity.badRequest().build();
        }

        //refactored sorting order
        students = sortStudents(students, sortBy, sortOrder);

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

//            Map<String, Object> response = new HashMap<>();
//            response.put("students", students);
//            response.put("totalPages", totalPages);
            Response response = new Response();
            response.setStudent(students);
            response.setTotalPage(totalPages);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(students.subList(0, Math.min(pageSize, totalStudents)));
        }
    }

    @Autowired
    public StudentService(StudentRepository repository) {
        this.studentRepository = repository;
    }

    public List<Student> retrieveStudent() {
        return (List<Student>) studentRepository.findAll();
    }

    public Optional<Student> retrieveStudent(Long id) {
        return studentRepository.findById(id);
    }

    public List<Student> retrieveStudent(String name) {
        return studentRepository.findByFirstName(name);
    }

    public Student createStudent(Student student) {
        student.setId(null);
        return studentRepository.save(student);
    }

    public Optional<Student> updateStudent(Long id, Student student) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if(!studentOptional.isPresent()) {
            return studentOptional;
        }
        student.setId(id);
        return Optional.of(studentRepository.save(student));
    }

    public boolean deleteStudent(Long id) {
        try {
            studentRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public long countStudents() {
        return studentRepository.count();
    }

    public List<Student> retrieveStudentByName(String name, Sort sort) {
        return studentRepository.findByFirstNameContainingIgnoreCase(name,sort);
    }
    
    public List<Student> retrieveStudentByLastName(String lastName, Sort sort) {
        return studentRepository.findByLastNameContainingIgnoreCase(lastName,sort);
    }

    public List<Student> retrieveStudentByNameAndLastName(String name, String lastName, Sort sort) {
        return studentRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(name, lastName,sort);
    }
}

