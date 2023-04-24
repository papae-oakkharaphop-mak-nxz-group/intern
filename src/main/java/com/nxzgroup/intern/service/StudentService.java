package com.nxzgroup.intern.service;

import java.util.*;

import com.nxzgroup.intern.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nxzgroup.intern.repository.StudentRepository;
import com.nxzgroup.intern.model.Student;

@Service
public class StudentService {
    private StudentRepository studentRepository;

    public enum StudentFilter {
        BY_NAME_AND_LASTNAME,
        BY_NAME,
        BY_LASTNAME,
        ALL;

        public static StudentFilter fromValues(String name, String lastName) {
            if (name != null && !name.isBlank() && lastName != null && !lastName.isBlank()) {
                return BY_NAME_AND_LASTNAME;
            } else if (name != null && !name.isBlank()) {
                return BY_NAME;
            } else if (lastName != null && !lastName.isBlank()) {
                return BY_LASTNAME;
            } else {
                return ALL;
            }
        }
    }

    public ResponseEntity<Object> getStudent(Long id){
        Optional<Student> student = retrieveStudent(id);
        if (!student.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }
    public ResponseEntity<Object> getAllStudents(Integer pageSize, Integer page, String name, String lastName, String sortBy, String sortOrder) {
        Pageable pageable;
        if (page == null) {
            Integer unknown = 100000;
            pageable = PageRequest.of(0, unknown, Sort.by(sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        } else {
            page = page - 1;
            pageable = PageRequest.of(page, pageSize, Sort.by(sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        }

        StudentFilter filter = StudentFilter.fromValues(name, lastName);

        Page<Student> studentPage;

        switch (filter) {
            case BY_NAME_AND_LASTNAME:
                studentPage = studentRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(name, lastName, pageable);
                break;
            case BY_NAME:
                studentPage = studentRepository.findByFirstNameContainingIgnoreCase(name, pageable);
                break;
            case BY_LASTNAME:
                studentPage = studentRepository.findByLastNameContainingIgnoreCase(lastName, pageable);
                break;
            case ALL:
                studentPage = studentRepository.findAll(pageable);
                break;
            default:
                return ResponseEntity.badRequest().build();
        }

        if (studentPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        int totalStudents = (int) studentPage.getTotalElements();
        int totalPages = studentPage.getTotalPages();

        Response response = new Response();
        response.setStudent(studentPage.getContent());
        response.setTotalPage(totalPages);
        response.setTotalStudent(totalStudents);
        return ResponseEntity.ok(response);
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

