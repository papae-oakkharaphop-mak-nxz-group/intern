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

    public ResponseEntity<Object> getStudent(Long id) {
        Optional<Student> student = retrieveStudent(id);
        if (!student.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    public Pageable getPage(Integer page, Integer pageSize, String sortOrder, String sortBy) {
        Pageable pageable;
        if (page == null) {
            Integer unknownPageSize = 100000;
            pageable = PageRequest.of(0, unknownPageSize, Sort.by(sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        } else {
            page = page - 1;
            pageable = PageRequest.of(page, pageSize, Sort.by(sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
        }
        return pageable;
    }

    public Response getAllStudents(Integer pageSize, Integer page, String name, String lastName, String sortBy, String sortOrder) throws Exception {
        try {
            StudentFilter filter = StudentFilter.fromValues(name, lastName);
            Pageable pageable = getPage(page, pageSize, sortOrder, sortBy);
            Page<Student> studentPage;
            Response response = new Response();

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
                    throw new Exception("Bad Request");
            }

            if (studentPage.isEmpty()) {
                throw new Exception("No Data");
            }

            int totalStudents = (int) studentPage.getTotalElements();
            int totalPages = studentPage.getTotalPages();


            response.setTotalPage(totalPages);
            response.setTotalStudent(totalStudents);
            response.setStudent(studentPage.getContent());
            return response;
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e.getCause());
        }
    }


    @Autowired
    public StudentService(StudentRepository repository) {
        this.studentRepository = repository;
    }

    public List<Student> retrieveStudent() {
        try {
            return (List<Student>) studentRepository.findAll();
        } catch (Exception e) {
            return null;
        }
    }

    public Optional<Student> retrieveStudent(Long id) {
        try {
            return studentRepository.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public Student createStudent(Student student) {
        try {
            student.setId(null);
            return studentRepository.save(student);
        } catch (Exception e) {
            return null;
        }
    }

    public Optional<Student> updateStudent(Long id, Student student) {
        try {
            Optional<Student> studentOptional = studentRepository.findById(id);
            if (!studentOptional.isPresent()) {
                return studentOptional;
            }
            student.setId(id);
            return Optional.of(studentRepository.save(student));
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteStudent(Long id) {
        try {
            studentRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}

