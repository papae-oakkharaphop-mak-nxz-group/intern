package com.nxzgroup.intern.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nxzgroup.intern.repository.StudentRepository;
import com.nxzgroup.intern.model.Student;

@Service
public class StudentService {
    private StudentRepository studentRepository;

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

