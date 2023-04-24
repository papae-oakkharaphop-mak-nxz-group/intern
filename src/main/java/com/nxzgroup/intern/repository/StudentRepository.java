package com.nxzgroup.intern.repository;

import java.util.List;
import com.nxzgroup.intern.model.Student;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
    List<Student> findByFirstName(String firstName);
    List<Student> findByFirstNameContainingIgnoreCase(String firstName, Sort sort);
    List<Student> findByLastNameContainingIgnoreCase(String lastName, Sort sort);
    List<Student> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName, Sort sort);
    
}